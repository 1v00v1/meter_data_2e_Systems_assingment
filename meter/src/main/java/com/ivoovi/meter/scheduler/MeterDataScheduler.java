package com.ivoovi.meter.scheduler;

import com.ivoovi.meter.domain.MeterDetailData;
import com.ivoovi.meter.repository.MeterDetailDataRepository;
import com.ivoovi.meter.repository.MeterRawDataRepository;
import com.ivoovi.meter.repository.SubscriptionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ivoovi.meter.domain.MeterRawData;
import com.ivoovi.meter.domain.Subscription;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class MeterDataScheduler {
    private static final Logger log = LoggerFactory.getLogger(MeterDataScheduler.class);

    private static final String CRON_EVERY_15_MIN = "0 */15 * * * *";

    private static final double MILES_TO_METERS = 1609.34;
    private static final int CLOUD_ALTITUDE_MULTIPLIER_FEET = 100;
    private static final String METAR_SOURCE_URL_TPL = "https://tgftp.nws.noaa.gov/data/observations/metar/stations/%s.TXT";

    private final SubscriptionRepository subscriptionRepository;
    private final MeterDetailDataRepository meterDetailDataRepository;
    private final MeterRawDataRepository meterRawDataRepository;
    private final RestTemplate restTemplate;

    public MeterDataScheduler(MeterDetailDataRepository meterDetailDataRepository, SubscriptionRepository subscriptionRepository, MeterRawDataRepository meterRawDataRepository, RestTemplate restTemplate) {
        this.meterDetailDataRepository = meterDetailDataRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.meterRawDataRepository = meterRawDataRepository;
        this.restTemplate = restTemplate;
    }

    private static final Pattern WIND_PATTERN = Pattern.compile("(VRB|\\d{3})(\\d{2})(G(\\d{2,3}))?KT(?:\\s+(\\d{3})V(\\d{3}))?");
    private static final Pattern VISIBILITY_PATTERN = Pattern.compile("(?:(\\d+)(SM))|(?:(\\d{4,5}))");
    private static final Pattern WEATHER_PATTERN = Pattern.compile("(?:(?:[-+]?)(RA|SN|DZ|FG|GS|BR|DU|UP|SA|SS|DS|PL|UP|VCTS|TS))");
    private static final Pattern CLOUD_PATTERN = Pattern.compile("(FEW|SCT|BKN|OVC)(\\d{3})");
    private static final Pattern TEMP_DEW_PATTERN = Pattern.compile("(M?\\d{2})/(M?\\d{2})");
    private static final Pattern PRESSURE_PATTERN = Pattern.compile("(A|Q)(\\d{4})");

    private static final Pattern RMK_PATTERN = Pattern.compile("RMK(.*)$");


    @Scheduled(cron = CRON_EVERY_15_MIN)
    public void fetchAndPersistMetar() {
        List<Subscription> subscriptions = subscriptionRepository.findByActive(true,null).getContent();
        if (subscriptions.isEmpty()) {
            log.debug("No active subscriptions found for METAR fetch.");
            return;
        }

        for (Subscription sub : subscriptions) {
            String icao = sub.getIcaoCode();
            String url = String.format(METAR_SOURCE_URL_TPL, icao);
            try {
                String body = restTemplate.getForObject(url, String.class);
                if (body == null || body.isBlank()) {
                    log.warn("Empty METAR response for ICAO {}", icao);
                    continue;
                }


                MeterRawData raw = new MeterRawData();
                raw.setRawData(body);
                raw.setSubscription(sub);
                meterRawDataRepository.save(raw);


                String metarLine = extractLatestMetar(body);
                if (metarLine == null) {
                    log.warn("No METAR line found in response for ICAO {}", icao);
                    continue;
                }

                MeterDetailData detail = parse(metarLine);
                if (detail == null) {
                    log.warn("Failed to parse METAR for ICAO {}. Line: {}", icao, metarLine);
                    continue;
                }


                detail.setSubscription(sub);
                if (detail.getTimeOfReport() == null) {

                    detail.setTimeOfReport(LocalDateTime.now());
                }

                meterDetailDataRepository.save(detail);

            } catch (Exception ex) {
                log.error("Error fetching/parsing METAR for ICAO {} from {}: {}", icao, url, ex.getMessage(), ex);
            }
        }
    }

    private String extractLatestMetar(String body) {
        String[] lines = body.split("\\R");
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i].trim();
            if (!line.isEmpty() && !line.startsWith("Last update")) {
                return line;
            }
        }
        return null;
    }

    public MeterDetailData parse(String metarLine) {
        if (metarLine == null || metarLine.trim().isEmpty()) {
            return null;
        }

        MeterDetailData data = new MeterDetailData();
        String[] parts = metarLine.trim().split("\\s+");


        for (int i = 1; i < parts.length; i++) {
            String token = parts[i];

            if (tryParseWind(token, data)) continue;
            if (tryParseVisibility(token, data)) continue;
            if (tryParseWeather(token, data)) continue;
            if (tryParseClouds(token, data)) continue;
            if (tryParseTempDew(token, data)) continue;
            if (tryParsePressure(token, data)) continue;
            if (tryParseRemarks(token, metarLine, data)) continue;
            tryParseRunway(token, data);
        }

        return data;
    }

    private boolean tryParseWind(String token, MeterDetailData data) {
        Matcher mWind = WIND_PATTERN.matcher(token);
        if (!mWind.matches()) return false;

        String dirOrVRB = mWind.group(1);
        String speed = mWind.group(2);
        String gust = mWind.group(4);

        if (!"VRB".equals(dirOrVRB)) {
            data.setWindDirection(Integer.parseInt(dirOrVRB));
        }
        data.setWindSpeed(Integer.parseInt(speed));
        if (gust != null) {
            data.setWindGust(Integer.parseInt(gust));
        }


        return true;
    }

    private boolean tryParseVisibility(String token, MeterDetailData data) {
        Matcher mVis = VISIBILITY_PATTERN.matcher(token);
        if (!mVis.matches()) return false;

        if (mVis.group(1) != null) {
            int miles = Integer.parseInt(mVis.group(1));
            int meters = (int) Math.round(miles * MILES_TO_METERS);
            data.setVisibilityInMeters(meters);
        } else if (mVis.group(3) != null) {
            int meters = Integer.parseInt(mVis.group(3));
            data.setVisibilityInMeters(meters);
        }
        return true;
    }

    private boolean tryParseWeather(String token, MeterDetailData data) {
        Matcher mWeather = WEATHER_PATTERN.matcher(token);
        if (!mWeather.find()) return false;
        data.setWeather(token);
        return true;
    }

    private boolean tryParseClouds(String token, MeterDetailData data) {
        Matcher mCloud = CLOUD_PATTERN.matcher(token);
        if (!mCloud.matches()) return false;

        String cover = mCloud.group(1);
        String alt = mCloud.group(2);
        data.setCloudCover(cover);
        data.setCloudAltitudeInFeet(Integer.parseInt(alt) * CLOUD_ALTITUDE_MULTIPLIER_FEET);
        return true;
    }

    private boolean tryParseTempDew(String token, MeterDetailData data) {
        Matcher mTemp = TEMP_DEW_PATTERN.matcher(token);
        if (!mTemp.matches()) return false;

        String t = mTemp.group(1);
        String d = mTemp.group(2);
        data.setTemperatureInCelsius(parseTemp(t));
        data.setDewPointInCelsius(parseTemp(d));
        return true;
    }

    private boolean tryParsePressure(String token, MeterDetailData data) {
        Matcher mPres = PRESSURE_PATTERN.matcher(token);
        if (!mPres.matches()) return false;

        String unit = mPres.group(1);
        String val = mPres.group(2);
        int hpa = convertPressureToHpa(unit, val);
        data.setPressureHpa(hpa);
        return true;
    }

    private boolean tryParseRemarks(String token, String fullLine, MeterDetailData data) {
        if (!token.startsWith("RMK")) return false;
        Matcher mRmk = RMK_PATTERN.matcher(fullLine);
        if (mRmk.find()) {
            String rmk = mRmk.group(1).trim();
            data.setRemarks(rmk.isEmpty() ? null : rmk);
        }
        return true;
    }

    private void tryParseRunway(String token, MeterDetailData data) {
        if (token.startsWith("R") || token.startsWith("RWY")) {
            String current = data.getRunwayData();
            String merged = (current == null ? token : current + " " + token);
            data.setRunwayData(merged);
        }
    }


    private BigDecimal parseTemp(String token) {
        boolean negative = token.startsWith("M");
        String num = negative ? token.substring(1) : token;
        int val = Integer.parseInt(num);
        return BigDecimal.valueOf(negative ? -val : val);
    }


    private int convertPressureToHpa(String unit, String val) {

        if ("A".equals(unit)) {
            int inHgTimes100 = Integer.parseInt(val); // e.g., 2992
            double inHg = inHgTimes100 / 100.0;
            double hpa = inHg * 33.8639;
            return (int) Math.round(hpa);
        } else if ("Q".equals(unit)) {

            return Integer.parseInt(val);
        }
        return 0;
    }

}
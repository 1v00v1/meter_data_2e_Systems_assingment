package com.ivoovi.meter.service;

import com.ivoovi.meter.dto.WeatherDto;
import com.ivoovi.meter.repository.MeterDetailDataRepository;
import com.ivoovi.meter.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final MeterDetailDataRepository meterDetailDataRepository;
    private final SubscriptionRepository subscriptionRepository;


    @Override
    public Optional<WeatherDto> getLatestWeatherDtoByIcaoCode(String icaoCode) {
        return subscriptionRepository.findById(icaoCode.toUpperCase())
                .flatMap(meterDetailDataRepository::findTop1BySubscriptionOrderByCreatedAtDesc)
                .map(WeatherDto::new);
    }

    public String toHumanReadableString(WeatherDto dto, Set<String> fields) {
        if (fields == null) fields = Collections.emptySet();
        StringBuilder sb = new StringBuilder();


        if ((fields.isEmpty() || fields.contains("windDirection")) && dto.getWindDirection() != null) {
            sb.append("Wind Direction: ").append(dto.getWindDirection()).append("°, ");
        }
        if ((fields.isEmpty() || fields.contains("windSpeed")) && dto.getWindSpeed() != null) {
            sb.append("Wind Speed: ").append(dto.getWindSpeed()).append(" km/h, ");
        }
        if ((fields.isEmpty() || fields.contains("windGust")) && dto.getWindGust() != null) {
            sb.append("Wind Gust: ").append(dto.getWindGust()).append(" km/h, ");
        }
        if ((fields.isEmpty() || fields.contains("visibilityInMeters")) && dto.getVisibilityInMeters() != null) {
            sb.append("Visibility: ").append(dto.getVisibilityInMeters()).append(" m, ");
        }
        if ((fields.isEmpty() || fields.contains("temperature")) && dto.getTemperature() != null) {
            sb.append("Temperature: ").append(dto.getTemperature()).append("°C, ");
        }
        if ((fields.isEmpty() || fields.contains("pressureHpa")) && dto.getPressureHpa() != null) {
            sb.append("Pressure: ").append(dto.getPressureHpa()).append(" hPa, ");
        }

        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }
}

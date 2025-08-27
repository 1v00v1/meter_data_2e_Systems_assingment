package com.ivoovi.meter.dto;

import com.ivoovi.meter.domain.MeterDetailData;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MeterDetailDataDto {


    private LocalDateTime timeOfReport;
    private String icaoCode;
    private Integer windDirection;
    private Integer windSpeed;
    private Integer windGust;

    private Integer visibilityInMeters;
    private String weather;
    private String cloudCover;
    private Integer cloudAltitudeInFeet;
    private BigDecimal temperatureInCelsius;
    private BigDecimal dewPointInCelsius;
    private Integer pressureHpa;
    private String runwayData;
    private String remarks;
    private LocalDateTime createdAt;

    public MeterDetailDataDto( MeterDetailData meterDetailData) {
        this.icaoCode = meterDetailData.getSubscription().getIcaoCode();
        this.timeOfReport = meterDetailData.getTimeOfReport();
        this.windDirection = meterDetailData.getWindDirection();
        this.windSpeed = meterDetailData.getWindSpeed();
        this.windGust = meterDetailData.getWindGust();
        this.visibilityInMeters = meterDetailData.getVisibilityInMeters();
        this.weather = meterDetailData.getWeather();
        this.cloudCover = meterDetailData.getCloudCover();
        this.cloudAltitudeInFeet = meterDetailData.getCloudAltitudeInFeet();
        this.temperatureInCelsius = meterDetailData.getTemperatureInCelsius();
        this.dewPointInCelsius = meterDetailData.getDewPointInCelsius();
        this.pressureHpa = meterDetailData.getPressureHpa();
        this.runwayData = meterDetailData.getRunwayData();
        this.remarks = meterDetailData.getRemarks();
        this.createdAt = meterDetailData.getCreatedAt();
    }

}

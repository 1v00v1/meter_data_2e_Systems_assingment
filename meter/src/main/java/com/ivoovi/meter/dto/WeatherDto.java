package com.ivoovi.meter.dto;

import com.ivoovi.meter.domain.MeterDetailData;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class WeatherDto {
    private Integer windDirection;
    private Integer windSpeed;
    private Integer windGust;
    private Integer visibilityInMeters;
    private BigDecimal temperature;
    private Integer pressureHpa;

    public WeatherDto(MeterDetailData meterDetailData) {
        this.windDirection = meterDetailData.getWindDirection();
        this.windSpeed = meterDetailData.getWindSpeed();
        this.windGust = meterDetailData.getWindGust();
        this.visibilityInMeters = meterDetailData.getVisibilityInMeters();
        this.temperature = meterDetailData.getTemperatureInCelsius();
        this.pressureHpa = meterDetailData.getPressureHpa();
    }
}

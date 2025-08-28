package com.ivoovi.meter.service;


import com.ivoovi.meter.dto.WeatherDto;
import com.ivoovi.meter.repository.MeterDetailDataRepository;
import com.ivoovi.meter.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class WeatherServiceImpl implements WeatherService{

    private final MeterDetailDataRepository meterDetailDataRepository;
    private final SubscriptionRepository subscriptionRepository;


    @Override
    public Optional<String> getLatestWeatherByIcaoCode(String icaoCode) {
        return subscriptionRepository.findById(icaoCode.toUpperCase())
                .flatMap(meterDetailDataRepository::findTop1BySubscriptionOrderByCreatedAtDesc)
                .map(meter -> {
                    StringBuilder sb = new StringBuilder();
                    if (meter.getWindDirection() != null) sb.append("Wind Direction: ").append(meter.getWindDirection()).append("°, ");
                    if (meter.getWindSpeed() != null) sb.append("Wind Speed: ").append(meter.getWindSpeed()).append(" km/h, ");
                    if (meter.getWindGust() != null) sb.append("Wind Gust: ").append(meter.getWindGust()).append(" km/h, ");
                    if (meter.getVisibilityInMeters() != null) sb.append("Visibility: ").append(meter.getVisibilityInMeters()).append(" m, ");
                    if (meter.getTemperatureInCelsius() != null) sb.append("Temperature: ").append(meter.getTemperatureInCelsius()).append("°C, ");
                    if (meter.getPressureHpa() != null) sb.append("Pressure: ").append(meter.getPressureHpa()).append(" hPa");

                    String result = sb.toString().trim();
                    if (result.endsWith(",")) result = result.substring(0, result.length() - 1);
                    return result;
                });
    }
}

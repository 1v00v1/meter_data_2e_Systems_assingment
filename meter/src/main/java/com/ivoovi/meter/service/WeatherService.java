package com.ivoovi.meter.service;
import java.util.Optional;


public interface WeatherService {

    Optional<String> getLatestWeatherByIcaoCode(String icaoCode);
}

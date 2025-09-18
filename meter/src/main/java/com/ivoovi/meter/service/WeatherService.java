package com.ivoovi.meter.service;
import com.ivoovi.meter.dto.WeatherDto;

import java.util.Optional;
import java.util.Set;


public interface WeatherService {

    String toHumanReadableString(WeatherDto dto, Set<String> fields);

    Optional<WeatherDto> getLatestWeatherDtoByIcaoCode(String icaoCode);
}

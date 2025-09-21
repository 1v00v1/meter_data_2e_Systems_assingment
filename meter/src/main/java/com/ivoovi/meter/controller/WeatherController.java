package com.ivoovi.meter.controller;

import com.ivoovi.meter.dto.WeatherDto;
import com.ivoovi.meter.service.WeatherService;
import com.ivoovi.meter.utility.JsonFilterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final JsonFilterUtil jsonFilterUtil;

    @GetMapping("/{icao}")
    public ResponseEntity<?> getLatestWeather(
            @PathVariable String icao,
            @RequestParam(required = false) String fields,
            @RequestParam(required = false, defaultValue = "true") boolean humanReadable
    ) throws Exception {

        Optional<WeatherDto> weatherDtoOpt = weatherService.getLatestWeatherDtoByIcaoCode(icao);

        if (weatherDtoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Set<String> fieldSet = (fields == null || fields.isBlank())
                ? Collections.emptySet()
                : Stream.of(fields.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());


        WeatherDto dto = weatherDtoOpt.get();

        if (humanReadable) {

            return ResponseEntity.ok(weatherService.toHumanReadableString(dto,fieldSet));
        }

        Map<String, Object> filtered = jsonFilterUtil.filterObject(dto, fields);
        return ResponseEntity.ok(filtered);
    }
}

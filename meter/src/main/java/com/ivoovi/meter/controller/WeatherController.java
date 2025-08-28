package com.ivoovi.meter.controller;


import com.ivoovi.meter.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/{icao}")
    public ResponseEntity<String> getLatestWeather(@PathVariable String icao) {
        return weatherService.getLatestWeatherByIcaoCode(icao)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

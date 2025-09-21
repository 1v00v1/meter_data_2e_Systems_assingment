package com.ivoovi.meter.controller;

import com.ivoovi.meter.dto.WeatherDto;
import com.ivoovi.meter.service.WeatherService;
import com.ivoovi.meter.utility.JsonFilterUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WeatherControllerTest {

    private static final String ICAO = "KJFK";

    @Mock
    private WeatherService weatherService;

    @Mock
    private JsonFilterUtil jsonFilterUtil;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void returns404WhenNoWeatherFound() throws Exception {
        when(weatherService.getLatestWeatherDtoByIcaoCode(ICAO)).thenReturn(Optional.empty());

        ResponseEntity<?> response = weatherController.getLatestWeather(ICAO, null, true);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verifyNoInteractions(jsonFilterUtil);
    }

    @Test
    void returnsHumanReadableByDefault() throws Exception {
        WeatherDto dto = new WeatherDto(180, 10, null, 8000, new BigDecimal("22.5"), 1013);
        when(weatherService.getLatestWeatherDtoByIcaoCode(ICAO)).thenReturn(Optional.of(dto));
        when(weatherService.toHumanReadableString(eq(dto), any(Set.class))).thenReturn("Wind 180/10, 22.5°C, 1013 hPa");

        ResponseEntity<?> response = weatherController.getLatestWeather(ICAO, "temperature,pressureHpa", true);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Wind 180/10, 22.5°C, 1013 hPa");
        verify(weatherService).toHumanReadableString(eq(dto), argThat(set -> set.contains("temperature") && set.contains("pressureHpa")));
        verifyNoInteractions(jsonFilterUtil);
    }

    @Test
    void returnsFilteredJsonWhenHumanReadableFalse() throws Exception {
        WeatherDto dto = new WeatherDto(200, 15, 25, 10000, new BigDecimal("18.0"), 1008);
        when(weatherService.getLatestWeatherDtoByIcaoCode(ICAO)).thenReturn(Optional.of(dto));
        Map<String, Object> filtered = Map.of("temperature", "18.0", "pressureHpa", 1008);
        when(jsonFilterUtil.filterObject(dto, "temperature,pressureHpa")).thenReturn(filtered);

        ResponseEntity<?> response = weatherController.getLatestWeather(ICAO, "temperature,pressureHpa", false);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(filtered);
        verify(jsonFilterUtil).filterObject(dto, "temperature,pressureHpa");
        verify(weatherService, never()).toHumanReadableString(any(), anySet());
    }
}
package com.ivoovi.meter.controller;

import com.ivoovi.meter.scheduler.MeterDataScheduler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meter")
@AllArgsConstructor
public class MeterDataController {

    private final MeterDataScheduler meterDataScheduler;

    @PostMapping
    public String fetchAndPersistMeterData(){
        meterDataScheduler.fetchAndPersistMetar();
        return "✅ METAR fetch job triggered!";
    }
}

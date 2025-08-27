package com.ivoovi.meter.dto;

import com.ivoovi.meter.domain.MeterRawData;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeterRawDataDto {

    private String rawData;
    private LocalDateTime created_at;
    private String ICAOCode;


    public MeterRawDataDto(MeterRawData meterRawData) {
        this.rawData = meterRawData.getRawData();
        this.created_at = meterRawData.getCreatedAt();
        this.ICAOCode = meterRawData.getSubscription().getIcaoCode();
    }
}

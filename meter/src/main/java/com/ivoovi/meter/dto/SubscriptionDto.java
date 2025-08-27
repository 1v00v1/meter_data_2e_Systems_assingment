package com.ivoovi.meter.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.ivoovi.meter.domain.Subscription;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionDto {

    private String icaoCode;


    public SubscriptionDto(Subscription subscription) {
        this.icaoCode = subscription.getIcaoCode();

    }
}

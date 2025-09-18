package com.ivoovi.meter.dto;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ivoovi.meter.deserializer.IntToBoolean;
import com.ivoovi.meter.domain.Subscription;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SubscriptionDto {

    private String icaoCode;
    @JsonDeserialize(using = IntToBoolean.class)
    private boolean active ;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt ;


    public SubscriptionDto(Subscription subscription) {
        this.icaoCode = subscription.getIcaoCode();
        this.active = subscription.getActive();
        this.createdAt = subscription.getCreatedAt();
    }
}

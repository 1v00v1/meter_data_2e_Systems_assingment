package com.ivoovi.meter.service;

import com.ivoovi.meter.dto.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {

    boolean deleteSubscription(String icaoCode);
    List<SubscriptionDto> getAllSubscriptions();
    SubscriptionDto getSubscription(String icaoCode);
    boolean createSubscription(String icaoCode);

}

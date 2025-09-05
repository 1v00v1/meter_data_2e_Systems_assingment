package com.ivoovi.meter.service;

import com.ivoovi.meter.dto.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {

    boolean deleteSubscription(String icaoCode);
    List<SubscriptionDto> getAllSubscriptions();
    SubscriptionDto getSubscription(String icaoCode);
    SubscriptionDto createSubscription(String icaoCode);
    SubscriptionDto subscribe(String icaoCode, Boolean active);
    SubscriptionDto unsubscribe(String icaoCode, Boolean active);
    List<SubscriptionDto> getSubscriptionsByIcaoCodeLike(String icaoCode);

}

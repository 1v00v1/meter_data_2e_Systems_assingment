package com.ivoovi.meter.service;

import com.ivoovi.meter.dto.SubscriptionDto;
import com.ivoovi.meter.utility.PageResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SubscriptionService {

    boolean deleteSubscription(String icaoCode);
    Page<SubscriptionDto> getAllSubscriptions(Pageable pageable, Boolean active);
    SubscriptionDto getSubscription(String icaoCode);
    SubscriptionDto createSubscription(String icaoCode);
    void subscribe(String icaoCode, Boolean active);
    void unsubscribe(String icaoCode, Boolean active);
    Page<SubscriptionDto> getSubscriptionsByIcaoCodeLike(String icaoCodeLike, Pageable pageable, Boolean active);

}

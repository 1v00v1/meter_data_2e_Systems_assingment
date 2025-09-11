package com.ivoovi.meter.service;

import com.ivoovi.meter.dto.SubscriptionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionService {

    boolean deleteSubscription(String icaoCode);
    Page<SubscriptionDto> getAllSubscriptions(Pageable pageable);
    SubscriptionDto getSubscription(String icaoCode);
    SubscriptionDto createSubscription(String icaoCode);
    SubscriptionDto subscribe(String icaoCode, Boolean active);
    SubscriptionDto unsubscribe(String icaoCode, Boolean active);
    List<SubscriptionDto> getSubscriptionsByIcaoCodeLike(String icaoCode);

}

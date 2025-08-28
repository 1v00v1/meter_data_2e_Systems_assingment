package com.ivoovi.meter.service;

import com.ivoovi.meter.domain.Subscription;
import com.ivoovi.meter.dto.SubscriptionDto;
import com.ivoovi.meter.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{


    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public boolean deleteSubscription(String icaoCode) {
        if(subscriptionRepository.findByIcaoCode(icaoCode).isPresent()){
            subscriptionRepository.deleteByIcaoCode(icaoCode);
            System.out.println("Deleted subscription for icao code: " + icaoCode);
            return true;
        }else {
            System.out.println("No subscription found for icao code: " + icaoCode);
            return false;
        }
    }

    @Override
    public List<SubscriptionDto> getAllSubscriptions() {
        return subscriptionRepository.findByActiveTrue().stream().map(SubscriptionDto::new).toList();
    }

    @Override
    public SubscriptionDto getSubscription(String icaoCode) {
        return subscriptionRepository.findByIcaoCode(icaoCode).map(SubscriptionDto::new).orElse(null);
    }

    @Override
    public boolean createSubscription(String icaoCode) {
        try{
            subscriptionRepository.save(new Subscription(icaoCode));
            System.out.println("Created subscription for icao code: " + icaoCode);
            return true;
        }catch (Exception e){
            System.out.println("Failed to create subscription for icao code: " + icaoCode);
            return false;
        }

    }

    @Override
    public boolean subscribe(String icaoCode, Boolean active) {
        var opt = subscriptionRepository.findByIcaoCode(icaoCode);
        Subscription sub = opt.orElse(null);
        if (sub == null) {
            return false;
        }
        sub.setActive(true);
        subscriptionRepository.save(sub);
        return true;
    }

    @Override
    public boolean unsubscribe(String icaoCode, Boolean active) {
        var opt = subscriptionRepository.findByIcaoCode(icaoCode);
        Subscription sub = opt.orElse(null);
        if (sub == null) {
            return false;
        }
        sub.setActive(false);
        subscriptionRepository.save(sub);
        return true;
    }

    @Override
    public List<SubscriptionDto> getSubscriptionsByIcaoCodeLike(String icaoCode) {
        return subscriptionRepository.findByIcaoCodeContainingAndActiveTrue(icaoCode).stream().map(SubscriptionDto::new).toList();
    }

}


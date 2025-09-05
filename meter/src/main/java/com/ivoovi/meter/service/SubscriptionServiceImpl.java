package com.ivoovi.meter.service;

import com.ivoovi.meter.domain.Subscription;
import com.ivoovi.meter.dto.SubscriptionDto;
import com.ivoovi.meter.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{


    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    @CacheEvict(value = {"subscriptions","subscription"},key = "#icaoCode")
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
    @Cacheable(value = "subscriptions")
    public List<SubscriptionDto> getAllSubscriptions() {
        return subscriptionRepository.findByActiveTrue().stream().map(SubscriptionDto::new).toList();
    }

    @Override
    @Cacheable(value = "subscription" , key = "#icaoCode")
    public SubscriptionDto getSubscription(String icaoCode) {
        return subscriptionRepository.findByIcaoCode(icaoCode).map(SubscriptionDto::new).orElse(null);
    }

    @Override
    @CachePut(value = "subscription" , key = "#icaoCode")
    @CacheEvict(value = "subscriptions",allEntries = true)
    public SubscriptionDto createSubscription(String icaoCode) {
        try{
            Subscription saved = subscriptionRepository.save(new Subscription(icaoCode));
            System.out.println("Created subscription for icao code: " + icaoCode);
            return new SubscriptionDto(saved);
        }catch (Exception e){
            System.out.println("Failed to create subscription for icao code: " + icaoCode);
            return null;
        }

    }

    @Override
    @CachePut(value = "subscription" , key = "#icaoCode")
    @CacheEvict(value = "subscriptions",allEntries = true)
    public SubscriptionDto subscribe(String icaoCode, Boolean active) {
        return subscriptionRepository.findByIcaoCode(icaoCode).map(sub -> {
            sub.setActive(active);
            subscriptionRepository.save(sub);
            return new SubscriptionDto(sub);
        }).orElse(null);

    }

    @Override
    @CachePut(value = "subscription" , key = "#icaoCode")
    @CacheEvict(value = "subscriptions",allEntries = true)
    public SubscriptionDto unsubscribe(String icaoCode, Boolean active) {
       return subscriptionRepository.findByIcaoCode(icaoCode).map(sub -> {
            sub.setActive(active);
            subscriptionRepository.save(sub);
            return new SubscriptionDto(sub);
       }).orElse(null);
    }

    @Override
    @Cacheable(value = "subscriptionsLike",key = "#icaoCode")
    public List<SubscriptionDto> getSubscriptionsByIcaoCodeLike(String icaoCode) {
        return subscriptionRepository.findByIcaoCodeContainingAndActiveTrue(icaoCode).stream().map(SubscriptionDto::new).toList();
    }

}


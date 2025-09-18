package com.ivoovi.meter.service;

import com.ivoovi.meter.domain.Subscription;
import com.ivoovi.meter.dto.SubscriptionDto;
import com.ivoovi.meter.repository.SubscriptionRepository;
import com.ivoovi.meter.utility.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{


    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    @CacheEvict(value = {"subscriptions","subscription"},key = "#icaoCode")
    public boolean deleteSubscription(String icaoCode) {
      if(!subscriptionRepository.existsById(icaoCode))return false;
      subscriptionRepository.deleteById(icaoCode);
      return true;
    }

    @Override
    public Page<SubscriptionDto> getAllSubscriptions(Pageable pageable, Boolean active) {
        Page<Subscription> page;

        if(active == null){
            page = subscriptionRepository.findAll(pageable);
        }else{
            page = subscriptionRepository.findByActive(active,pageable);
        }
       Page<SubscriptionDto> dtoPage = page.map(SubscriptionDto::new);
        return page.map(SubscriptionDto::new);

    }


    @Override
    @Cacheable(value = "subscription" , key = "#icaoCode")
    public SubscriptionDto getSubscription(String icaoCode) {
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode).orElseThrow(() -> new EntityNotFoundException("Subscription not found for ICAO: " + icaoCode));

        return new SubscriptionDto(subscription);
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
    public void subscribe(String icaoCode, Boolean active) {
        Optional<Subscription> optionalSubscription  = subscriptionRepository.findByIcaoCode(icaoCode);
        Subscription subscription = optionalSubscription
                .orElseThrow(() -> new IllegalArgumentException("No subscription found for ICAO code: " + icaoCode));

        subscription.setActive(active);
        subscriptionRepository.save(subscription);
    }

    @Override
    @CachePut(value = "subscription" , key = "#icaoCode")
    @CacheEvict(value = "subscriptions",allEntries = true)
    public void unsubscribe(String icaoCode, Boolean active) {
        subscribe(icaoCode, active);
    }

    @Override
    public Page<SubscriptionDto> getSubscriptionsByIcaoCodeLike(String icaoCodeLike, Pageable pageable, Boolean active) {
        Page<Subscription> page;

        if (active == null) {
            page = subscriptionRepository.findByIcaoCodeContaining(icaoCodeLike, pageable);

        } else {
            page = subscriptionRepository.findByIcaoCodeContainingAndActive(icaoCodeLike, active, pageable);
        }

        Page<SubscriptionDto> dtoPage = page.map(SubscriptionDto::new);
        return page.map(SubscriptionDto::new);
    }


}


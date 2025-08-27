package com.ivoovi.meter.controller;

import com.ivoovi.meter.dto.SubscriptionDto;
import com.ivoovi.meter.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

@GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAllActiveSubscriptions(){
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/{icaoCode}")
    public ResponseEntity<SubscriptionDto> getSubscription(@PathVariable String icaoCode){
    return ResponseEntity.ok(subscriptionService.getSubscription(icaoCode));
    }
    @DeleteMapping("/{icaoCode}")
    public ResponseEntity<Boolean> deleteSubscription(@PathVariable String icaoCode){
        return ResponseEntity.ok(subscriptionService.deleteSubscription(icaoCode));
    }

    @PostMapping("/{icaoCode}")
    public ResponseEntity<Boolean> createSubscription(@PathVariable String icaoCode){
    return ResponseEntity.ok(subscriptionService.createSubscription(icaoCode));
    }
}

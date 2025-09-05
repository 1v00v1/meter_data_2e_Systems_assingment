package com.ivoovi.meter.controller;

import com.ivoovi.meter.dto.SubscriptionDto;
import com.ivoovi.meter.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @GetMapping("/matching")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptionsByIcaoCodeLike(@RequestParam String icaoCode){
    return ResponseEntity.ok(subscriptionService.getSubscriptionsByIcaoCodeLike(icaoCode));
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
    public ResponseEntity<SubscriptionDto> createSubscription(@PathVariable String icaoCode){
    return ResponseEntity.ok(subscriptionService.createSubscription(icaoCode));
    }
    @PutMapping("/subscribe/{icaoCode}")
    public ResponseEntity<?> subscribe (@PathVariable String icaoCode, @RequestBody SubscriptionDto body
    ){
        subscriptionService.subscribe(icaoCode,body.isActive() );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/unsubscribe/{icaoCode}")
    public ResponseEntity<?> unsubscribe (@PathVariable String icaoCode, @RequestBody SubscriptionDto body
    ){
        subscriptionService.unsubscribe(icaoCode, body.isActive());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

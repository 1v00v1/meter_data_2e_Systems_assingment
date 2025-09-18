package com.ivoovi.meter.controller;

import com.ivoovi.meter.dto.SubscriptionDto;
import com.ivoovi.meter.service.SubscriptionService;
import com.ivoovi.meter.utility.JsonFilterUtil;
import com.ivoovi.meter.utility.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<?> getAllSubscriptions(
            @RequestParam(required = false) String icaoCode,
            @RequestParam(required = false) String icaoCodeLike,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String fields,
            Pageable pageable) throws Exception {

        Object result;

        if (icaoCode != null) {
            SubscriptionDto subscription = subscriptionService.getSubscription(icaoCode);


            if (active != null && subscription.isActive() != active) {
                return ResponseEntity.ok().body(null); // or ResponseEntity.noContent().build()
            }
            result = subscription;


            if (fields != null && !fields.isBlank()) {
                result = JsonFilterUtil.filterObject(subscription, fields);
            }

        } else if (icaoCodeLike != null) {

            Page<SubscriptionDto> page = subscriptionService.getSubscriptionsByIcaoCodeLike(icaoCodeLike, pageable, active);


            if (fields != null && !fields.isBlank()) {
                List<Map<String, Object>> filteredContent = page.getContent().stream()
                        .map(dto -> {
                            try {
                                return JsonFilterUtil.filterObject(dto, fields);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());

                PageResponse<Map<String, Object>> response = new PageResponse<>();
                response.setContent(filteredContent);
                response.setPage(page.getNumber());
                response.setSize(page.getSize());
                response.setTotalElements(page.getTotalElements());
                response.setTotalPages(page.getTotalPages());

                result = response;
            } else {
                result = new PageResponse<>(page);
            }

        } else {
            // All subscriptions
            Page<SubscriptionDto> page = subscriptionService.getAllSubscriptions(pageable, active);

            // Apply field filtering
            if (fields != null && !fields.isBlank()) {
                List<Map<String, Object>> filteredContent = page.getContent().stream()
                        .map(dto -> {
                            try {
                                return JsonFilterUtil.filterObject(dto, fields);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());

                PageResponse<Map<String, Object>> response = new PageResponse<>();
                response.setContent(filteredContent);
                response.setPage(page.getNumber());
                response.setSize(page.getSize());
                response.setTotalElements(page.getTotalElements());
                response.setTotalPages(page.getTotalPages());

                result = response;
            } else {
                result = new PageResponse<>(page);
            }
        }

        return ResponseEntity.ok(result);
    }


    @PostMapping("/{icaoCode}")
    public ResponseEntity<?> createSubscription(
            @PathVariable String icaoCode,
            @RequestParam(required = false) String fields
    ) throws Exception {

        // Create subscription
        SubscriptionDto subscription = subscriptionService.createSubscription(icaoCode);

        Object result;

        if (fields != null && !fields.isBlank()) {
            // Apply dynamic field filtering
            result = JsonFilterUtil.filterObject(subscription, fields);
        } else {
            result = subscription;
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{icaoCode}")
    public ResponseEntity<Boolean> deleteSubscription(@PathVariable String icaoCode) {
        return ResponseEntity.ok(subscriptionService.deleteSubscription(icaoCode));
    }
    @PutMapping("/{icaoCode}")
    public ResponseEntity<?> updateSubscriptionStatus(
            @PathVariable String icaoCode,
            @RequestBody SubscriptionDto body
    ) {

        if (body.isActive()) {
            subscriptionService.subscribe(icaoCode, true);
        } else {
            subscriptionService.unsubscribe(icaoCode, false);
        }

        return ResponseEntity.ok().build();
    }
}

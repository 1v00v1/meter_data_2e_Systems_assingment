package com.ivoovi.meter.repository;

import com.ivoovi.meter.domain.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    Optional<Subscription > findByIcaoCode(String icaoCode);
    List<Subscription> findByActiveTrue();
    void deleteByIcaoCode(String icaoCode);
    Page<Subscription> findByIcaoCodeContainingAndActiveTrue(String icaoCode, Pageable pageable);
}

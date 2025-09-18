package com.ivoovi.meter.repository;

import com.ivoovi.meter.domain.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    Page<Subscription> findByIcaoCodeContaining(String icaoCodeLike, Pageable pageable);
    Page<Subscription> findByActive(Boolean active, Pageable pageable);
    void deleteByIcaoCode(String icaoCode);
    Page<Subscription> findByIcaoCodeContainingAndActive(String icaoCode,Boolean active, Pageable pageable);
    Optional<Subscription> findByIcaoCode(String icaoCode);
}

package com.ivoovi.meter.repository;

import com.ivoovi.meter.domain.MeterDetailData;
import com.ivoovi.meter.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface MeterDetailDataRepository extends JpaRepository<MeterDetailData, Long> {

    Optional<MeterDetailData> findTop1BySubscriptionOrderByCreatedAtDesc(Subscription subscription);

}


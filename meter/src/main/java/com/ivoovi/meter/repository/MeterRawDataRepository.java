package com.ivoovi.meter.repository;

import com.ivoovi.meter.domain.MeterRawData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterRawDataRepository extends JpaRepository<MeterRawData, Long> {
}

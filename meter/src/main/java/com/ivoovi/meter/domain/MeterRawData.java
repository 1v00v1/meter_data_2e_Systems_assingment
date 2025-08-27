package com.ivoovi.meter.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "meter_raw_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MeterRawData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "raw_data", nullable = false)
    private String rawData;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icao_code" ,referencedColumnName = "icao_code")
    private Subscription subscription;
}

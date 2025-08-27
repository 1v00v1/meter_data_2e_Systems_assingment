package com.ivoovi.meter.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meter_detail_data")
public class MeterDetailData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icao_code", referencedColumnName = "icao_code")
    private Subscription subscription;

    @Column(name = "time_of_report", nullable = false)
    private LocalDateTime timeOfReport;

    @Column(name = "wind_direction")
    private Integer windDirection;

    @Column(name = "wind_speed")
    private Integer windSpeed;

    @Column(name = "wind_gust")
    private Integer windGust;

    @Column(name = "visibility_in_meters")
    private Integer visibilityInMeters;

    @Column(name = "weather")
    private String weather;

    @Column(name = "cloud_cover")
    private String cloudCover;

    @Column(name = "cloud_altitude_in_feet")
    private Integer cloudAltitudeInFeet;


    @Column(name = "temperature_in_celsius")
    private BigDecimal temperatureInCelsius;

    @Column(name = "dew_point_in_celsius")
    private BigDecimal dewPointInCelsius;

    @Column(name = "pressure_hpa")
    private Integer pressureHpa;

    @Column(name = "runway_data")
    private String runwayData;

    @Column(name = "remarks")
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
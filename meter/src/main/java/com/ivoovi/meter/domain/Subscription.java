package com.ivoovi.meter.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {


    @Id
    @Column(name = "icao_code" , length = 4)
    private String icaoCode;

    @Column(name = "active" ,nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MeterDetailData> meterDetailData;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MeterRawData> meterRawData;

    public Subscription(String icaoCode) {
        this.icaoCode = icaoCode;
    }
}

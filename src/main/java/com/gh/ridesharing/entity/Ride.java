package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.RideStatus;
import com.gh.ridesharing.enums.RideType;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "ride_requests")
public class Ride extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pickUpLocation;
    private String dropOffLocation;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private double fare;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @Enumerated(EnumType.STRING)
    private RideType type;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Driver driver;

    private int rating;
}


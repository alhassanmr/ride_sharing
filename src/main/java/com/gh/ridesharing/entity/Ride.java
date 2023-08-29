package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.RideStatus;
import com.gh.ridesharing.enums.RideType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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


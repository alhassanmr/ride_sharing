package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.RideRequestStatus;
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
    private String fromLocation;
    private String toLocation;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private double fare;

    @Enumerated(EnumType.STRING)
    private RideRequestStatus status;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Driver assignedDriver;

    private int rating;
}


package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.RideStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "ride_requests")
public class RideRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fromLocation;
    private String toLocation;
    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Driver assignedDriver;
}


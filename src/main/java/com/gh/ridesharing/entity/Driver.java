package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.DriverStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Data
public class Driver extends BaseEntity {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String driverLicense;

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    private String vehicleMake;
    private String vehicleModel;
    private String vehiclePlateNumber;

    private double averageRating;
    private boolean isAvailable;

}


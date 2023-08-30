package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.AvailabilityStatus;
import com.gh.ridesharing.enums.Gender;
import com.gh.ridesharing.enums.VehicleType;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Data
public class Driver extends BaseEntity {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String driverLicense;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus status;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String vehicleMake;
    private String vehicleModel;
    private String vehiclePlateNumber;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private double latitude;
    private double longitude;

    private double averageRating;
    private int rating;
    private int totalRides;

}

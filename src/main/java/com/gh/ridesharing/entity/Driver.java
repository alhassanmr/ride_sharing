package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.DriverStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Entity
@Data
public class Driver extends BaseEntity {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String driverLicense;

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus; // Enum for driver status (e.g., ACTIVE, INACTIVE)

    private String vehicleMake;
    private String vehicleModel;
    private String vehiclePlateNumber;

    private double averageRating;

}


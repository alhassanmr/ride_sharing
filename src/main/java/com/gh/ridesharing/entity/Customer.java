package com.gh.ridesharing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Customer extends BaseEntity {
    private String firstName;
    private String lastName;

    private String phoneNumber;
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;
}

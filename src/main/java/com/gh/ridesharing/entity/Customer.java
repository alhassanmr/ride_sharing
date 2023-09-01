package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.Gender;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.OneToOne;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@DiscriminatorValue("Customer")
public class Customer extends User {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}

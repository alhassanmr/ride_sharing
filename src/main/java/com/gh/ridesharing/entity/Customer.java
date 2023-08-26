package com.gh.ridesharing.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
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

package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.RoleType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Entity
@Data
public class User extends BaseEntity {

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String phoneNumber;
    private String profilePictureUrl;

    private String address;
    private String city;
    private String country;

    private boolean isActive;

}

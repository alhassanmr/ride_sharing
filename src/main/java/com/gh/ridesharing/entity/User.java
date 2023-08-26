package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.RoleType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "users")
public class User extends BaseEntity {
    @Column(unique = true)
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

    private boolean isActive = true;

}

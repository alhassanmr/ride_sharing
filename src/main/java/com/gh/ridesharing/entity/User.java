package com.gh.ridesharing.entity;

import com.gh.ridesharing.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;

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

    public User() {
    }
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

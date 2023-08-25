package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverService extends BaseServiceImpl<Driver> {

    private final DriverRepository driverRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository) {
        super(driverRepository);
        this.driverRepository = driverRepository;
    }

    public Optional<Driver> findByDriverLicense(String driverLicense) {
        return driverRepository.findByDriverLicense(driverLicense);
    }
}

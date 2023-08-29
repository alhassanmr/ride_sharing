package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.enums.AvailabilityStatus;
import com.gh.ridesharing.repository.DriverRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService extends BaseServiceImpl<Driver> {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        super(driverRepository);
        this.driverRepository = driverRepository;
    }

    public Optional<Driver> getById(Long driverId) {
        return driverRepository.findById(driverId);
    }

    public Driver setAvailability(Long driverId, AvailabilityStatus status) {
        Optional<Driver> optionalDriver = getById(driverId);
        if (optionalDriver.isPresent()) {
            Driver driver = optionalDriver.get();
            driver.setStatus(status);
            return driverRepository.save(driver);
        }
        throw new EntityNotFoundException("Driver not found with ID: " + driverId);
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }
}

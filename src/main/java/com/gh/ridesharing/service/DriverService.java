package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.enums.AvailabilityStatus;
import com.gh.ridesharing.repository.DriverRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService extends BaseServiceImpl<Driver> {

    private final DriverRepository driverRepository;

    // Constructor injection for the repository
    public DriverService(DriverRepository driverRepository) {
        super(driverRepository);
        this.driverRepository = driverRepository;
    }

    /**
     * Get a driver by ID.
     *
     * @param driverId The ID of the driver to retrieve.
     * @return The retrieved driver, if found.
     */
    public Optional<Driver> getById(Long driverId) {
        return driverRepository.findById(driverId);
    }

    /**
     * Set the availability status of a driver.
     *
     * @param driverId The ID of the driver to update.
     * @param status   The new availability status to set.
     * @return The updated driver.
     * @throws EntityNotFoundException if the driver is not found.
     */
    public Driver setAvailability(Long driverId, AvailabilityStatus status) {
        Optional<Driver> optionalDriver = getById(driverId);
        if (optionalDriver.isPresent()) {
            Driver driver = optionalDriver.get();
            driver.setStatus(status);
            return driverRepository.save(driver);
        }
        throw new EntityNotFoundException("Driver not found with ID: " + driverId);
    }

    /**
     * Get a list of all drivers.
     *
     * @return List of all drivers.
     */
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }
    public List<Driver> findByUserId(Long userId) {
        return driverRepository.findAll();
    }
}

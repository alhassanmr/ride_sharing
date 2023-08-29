package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.enums.AvailabilityStatus;
import com.gh.ridesharing.service.DriverService;
import com.gh.ridesharing.service.RideHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/drivers")
@Slf4j
public class DriverController {

    private final DriverService driverService;
    private final RideHistoryService rideHistoryService;

    // Constructor injection for services
    public DriverController(DriverService driverService, RideHistoryService rideHistoryService) {
        this.driverService = driverService;
        this.rideHistoryService = rideHistoryService;
    }

    // Endpoint to create a new driver
    @PostMapping
    public ResponseEntity<Driver> createDriver(@RequestBody Driver newDriver) {
        log.info("Request to create a new driver");
        Driver driver = driverService.create(newDriver);
        return ResponseEntity.ok(driver);
    }

    // Endpoint to retrieve a driver by ID
    @GetMapping("/{driverId}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long driverId) {
        log.info("Request to get driver by ID: {}", driverId);
        Optional<Driver> driver = driverService.getById(driverId);
        return driver.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to update an existing driver
    @PutMapping("/{driverId}")
    public ResponseEntity<Driver> updateDriver(@PathVariable Long driverId, @RequestBody Driver updatedDriver) {
        log.info("Request to update driver with ID: {}", driverId);
        Driver driver = driverService.update(driverId, updatedDriver);
        return ResponseEntity.ok(driver);
    }

    // Endpoint to delete a driver by ID
    @DeleteMapping("/{driverId}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long driverId) {
        log.info("Request to delete driver with ID: {}", driverId);
        driverService.deleteById(driverId);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to set driver availability
    @PutMapping("/{driverId}/availability")
    public ResponseEntity<Driver> setDriverAvailability(@PathVariable Long driverId, @RequestParam AvailabilityStatus status) {
        log.info("Request to set driver availability for ID: {}", driverId);
        Driver updatedDriver = driverService.setAvailability(driverId, status);
        return ResponseEntity.ok(updatedDriver);
    }

    // Endpoint to retrieve all drivers
    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {
        log.info("Request to get all drivers");
        List<Driver> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }
}

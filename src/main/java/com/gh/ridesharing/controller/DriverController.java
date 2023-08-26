package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.enums.DriverStatus;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/drivers")
@Slf4j
public class DriverController {

    private final DriverService driverService;
    private final RideHistoryService rideHistoryService;
    public DriverController(DriverService driverService, RideHistoryService rideHistoryService) {
        this.driverService = driverService;
        this.rideHistoryService = rideHistoryService;
    }

    @PostMapping
    public ResponseEntity<Driver> createDriver(@RequestBody Driver newDriver) {
        log.info("Request to create a new driver");
        Driver driver = driverService.create(newDriver);
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long driverId) {
        log.info("Request to get driver by ID: {}", driverId);
        Optional<Driver> driver = driverService.getById(driverId);
        return driver.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{driverId}")
    public ResponseEntity<Driver> updateDriver(@PathVariable Long driverId, @RequestBody Driver updatedDriver) {
        log.info("Request to update driver with ID: {}", driverId);
        Driver driver = driverService.update(driverId, updatedDriver);
        return ResponseEntity.ok(driver);
    }

    @DeleteMapping("/{driverId}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long driverId) {
        log.info("Request to delete driver with ID: {}", driverId);
        driverService.deleteById(driverId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{driverId}/availability")
    public ResponseEntity<Driver> setDriverAvailability(@PathVariable Long driverId, @RequestParam boolean available) {
        log.info("Request to set driver availability for ID: {}", driverId);
        Driver updatedDriver = driverService.setAvailability(driverId, available);
        return ResponseEntity.ok(updatedDriver);
    }

    @PutMapping("/{driverId}/status")
    public ResponseEntity<Driver> setDriverStatus(@PathVariable Long driverId, @RequestParam DriverStatus status) {
        log.info("Request to set driver status for ID: {}", driverId);
        Driver updatedDriver = driverService.setStatus(driverId, status);
        return ResponseEntity.ok(updatedDriver);
    }
}

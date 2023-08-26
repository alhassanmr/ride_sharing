package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/drivers")
@Slf4j
public class DriverController {

    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
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
}

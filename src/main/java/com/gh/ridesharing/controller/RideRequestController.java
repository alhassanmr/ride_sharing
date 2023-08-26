package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.RideRequest;
import com.gh.ridesharing.service.DriverService;
import com.gh.ridesharing.service.RideRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/ride-requests")
public class RideRequestController {
    private final RideRequestService rideRequestService;
    private final DriverService driverService;

    public RideRequestController(RideRequestService rideRequestService, DriverService driverService) {
        this.rideRequestService = rideRequestService;
        this.driverService = driverService;
    }

    @PutMapping("/{rideRequestId}/assign-driver/{driverId}")
    public ResponseEntity<RideRequest> assignDriverToRideRequest(
            @PathVariable Long rideRequestId,
            @PathVariable Long driverId) {

        Driver driver = driverService.getById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        RideRequest assignedRideRequest = rideRequestService.assignDriverToRideRequest(rideRequestId, driver);
        return ResponseEntity.ok(assignedRideRequest);
    }

}


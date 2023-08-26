package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.enums.RideRequestStatus;
import com.gh.ridesharing.service.DriverService;
import com.gh.ridesharing.service.RideRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/ride-requests")
@Slf4j
public class RideRequestController {
    private final RideRequestService rideRequestService;
    private final DriverService driverService;

    public RideRequestController(RideRequestService rideRequestService, DriverService driverService) {
        this.rideRequestService = rideRequestService;
        this.driverService = driverService;
    }

    @PutMapping("/{rideRequestId}/assign-driver/{driverId}")
    public ResponseEntity<Ride> assignDriverToRideRequest(
            @PathVariable Long rideRequestId,
            @PathVariable Long driverId) {

        Driver driver = driverService.getById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        Ride assignedRide = rideRequestService.assignDriverToRideRequest(rideRequestId, driver);
        return ResponseEntity.ok(assignedRide);
    }

    @PutMapping("/{rideRequestId}/accept")
    public ResponseEntity<Ride> acceptRideRequest(@PathVariable Long rideRequestId) {
        log.info("Driver accepting ride request with ID: {}", rideRequestId);
        Ride updatedRide = rideRequestService.updateRideRequestStatus(rideRequestId, RideRequestStatus.ACCEPTED);
        return ResponseEntity.ok(updatedRide);
    }

    @PutMapping("/{rideRequestId}/reject")
    public ResponseEntity<Ride> rejectRideRequest(@PathVariable Long rideRequestId) {
        log.info("Driver rejecting ride request with ID: {}", rideRequestId);
        Ride updatedRide = rideRequestService.updateRideRequestStatus(rideRequestId, RideRequestStatus.REJECTED);
        return ResponseEntity.ok(updatedRide);
    }

    @PutMapping("/{rideId}/rate")
    public ResponseEntity<Ride> rateRide(@PathVariable Long rideRequestId, @RequestParam int rating) {
        Ride ratedRide = rideRequestService.rateRideRequest(rideRequestId, rating);
        return ResponseEntity.ok(ratedRide);
    }
}


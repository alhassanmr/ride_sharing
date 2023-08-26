package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.RideRequest;
import com.gh.ridesharing.repository.RideRequestRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class RideRequestService {

    private final RideRequestRepository rideRequestRepository;
    private final DriverService driverService;

    public RideRequestService(RideRequestRepository rideRequestRepository, DriverService driverService) {
        this.rideRequestRepository = rideRequestRepository;
        this.driverService = driverService;
    }

    public RideRequest createRideRequest(RideRequest newRideRequest) {
        // Additional logic and validation if needed
        return rideRequestRepository.save(newRideRequest);
    }

    public Optional<RideRequest> getRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId);
    }

    public RideRequest updateRideRequest(Long rideRequestId, RideRequest updatedRideRequest) {
        // Additional logic and validation if needed
        RideRequest existingRideRequest = getRideRequestById(rideRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Ride Request not found."));
        // Update the existingRideRequest object with fields from updatedRideRequest
        return rideRequestRepository.save(existingRideRequest);
    }

    public void deleteRideRequest(Long rideRequestId) {
        rideRequestRepository.deleteById(rideRequestId);
    }
    public RideRequest assignDriverToRideRequest(Long rideRequestId, Driver driver) {
        RideRequest rideRequest = rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Ride request not found"));

        rideRequest.setAssignedDriver(driver);
        return rideRequestRepository.save(rideRequest);
    }

}


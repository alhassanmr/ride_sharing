package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.RideRequest;
import com.gh.ridesharing.enums.RideRequestStatus;
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

    public RideRequest getRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Ride request with ID " + rideRequestId + " not found."));
    }

    public RideRequest updateRideRequest(Long rideRequestId, RideRequest updatedRideRequest) {
        // Additional logic and validation if needed
        RideRequest existingRideRequest = getRideRequestById(rideRequestId);
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
    public RideRequest updateRideRequestStatus(Long rideRequestId, RideRequestStatus newStatus) {
        RideRequest rideRequest = getRideRequestById(rideRequestId);
        rideRequest.setStatus(newStatus);
        return rideRequestRepository.save(rideRequest);
    }
    public RideRequest rateRideRequest(Long rideRequestId, int rating) {
        RideRequest rideRequest = getRideRequestById(rideRequestId);

        if (rideRequest.getStatus() != RideRequestStatus.COMPLETED) {
            throw new IllegalStateException("Ride request is not completed.");
        }

        if (rideRequest.getRating() != 0) {
            throw new IllegalStateException("Ride request has already been rated.");
        }

        rideRequest.setRating(rating);
        rideRequestRepository.save(rideRequest);

        if (rideRequest.getAssignedDriver() != null) {
            Driver driver = rideRequest.getAssignedDriver();
            int currentDriverRating = driver.getRating();
            int totalRides = driver.getTotalRides();

            int newDriverRating = ((currentDriverRating * totalRides) + rating) / (totalRides + 1);
            driver.setRating(newDriverRating);
            driver.setTotalRides(totalRides + 1);
            driverService.update(driver.getId(), driver);
        }

        return rideRequest;
    }



}


package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.enums.RideRequestStatus;
import com.gh.ridesharing.repository.RideRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class RideRequestService {

    private final RideRepository rideRepository;
    private final DriverService driverService;

    public RideRequestService(RideRepository rideRepository, DriverService driverService) {
        this.rideRepository = rideRepository;
        this.driverService = driverService;
    }

    public Ride createRideRequest(Ride newRide) {
        // Additional logic and validation if needed
        return rideRepository.save(newRide);
    }

    public Ride getRideRequestById(Long rideRequestId) {
        return rideRepository.findById(rideRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Ride request with ID " + rideRequestId + " not found."));
    }

    public Ride updateRideRequest(Long rideRequestId, Ride updatedRide) {
        // Additional logic and validation if needed
        Ride existingRide = getRideRequestById(rideRequestId);
        // Update the existingRideRequest object with fields from updatedRideRequest
        return rideRepository.save(existingRide);
    }

    public void deleteRideRequest(Long rideRequestId) {
        rideRepository.deleteById(rideRequestId);
    }
    public Ride assignDriverToRideRequest(Long rideRequestId, Driver driver) {
        Ride ride = rideRepository.findById(rideRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Ride request not found"));

        ride.setDriver(driver);
        return rideRepository.save(ride);
    }
    public Ride updateRideRequestStatus(Long rideRequestId, RideRequestStatus newStatus) {
        Ride ride = getRideRequestById(rideRequestId);
        ride.setStatus(newStatus);
        return rideRepository.save(ride);
    }
    public Ride rateRideRequest(Long rideRequestId, int rating) {
        Ride ride = getRideRequestById(rideRequestId);

        if (ride.getStatus() != RideRequestStatus.COMPLETED) {
            throw new IllegalStateException("Ride request is not completed.");
        }

        if (ride.getRating() != 0) {
            throw new IllegalStateException("Ride request has already been rated.");
        }

        ride.setRating(rating);
        rideRepository.save(ride);

        if (ride.getDriver() != null) {
            Driver driver = ride.getDriver();
            int currentDriverRating = driver.getRating();
            int totalRides = driver.getTotalRides();

            int newDriverRating = ((currentDriverRating * totalRides) + rating) / (totalRides + 1);
            driver.setRating(newDriverRating);
            driver.setTotalRides(totalRides + 1);
            driverService.update(driver.getId(), driver);
        }

        return ride;
    }



}


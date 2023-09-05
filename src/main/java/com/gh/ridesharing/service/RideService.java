package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.enums.FeedbackRating;
import com.gh.ridesharing.enums.RideStatus;
import com.gh.ridesharing.repository.DriverRepository;
import com.gh.ridesharing.repository.RideRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gh.ridesharing.enums.AvailabilityStatus.AVAILABLE;
import static com.gh.ridesharing.enums.AvailabilityStatus.UNAVAILABLE;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverService driverService;
    private final DriverRepository driverRepository;

    // Constructor injection for the repositories and services
    public RideService(RideRepository rideRepository, DriverService driverService, DriverRepository driverRepository) {
        this.rideRepository = rideRepository;
        this.driverService = driverService;
        this.driverRepository = driverRepository;
    }

    // Method to create a new ride
    public Ride createRide(Ride newRide) {
        // Additional logic and validation if needed
        return rideRepository.save(newRide);
    }

    // Method to get a ride by its ID
    public Ride getRideById(Long rideRequestId) {
        return rideRepository.findById(rideRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Ride request with ID " + rideRequestId + " not found."));
    }

    // Method to update a ride
    public Ride updateRide(Ride updatedRide) {
        // Update the existingRideRequest object with fields from updatedRide
        return rideRepository.save(updatedRide);
    }

    // Method to delete a ride request
    public void deleteRideRequest(Long rideRequestId) {
        rideRepository.deleteById(rideRequestId);
    }

    // Method to rate a ride request
    public Ride rateRideRequest(Long rideRequestId, int rating) {
        Ride ride = getRideById(rideRequestId);

        if (ride.getStatus() != RideStatus.COMPLETED) {
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

    // Method to calculate distance between two points using Haversine formula
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the Earth in kilometers
        double earthRadius = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    // Method to find nearby drivers within a certain distance
    public List<Driver> findNearbyDrivers(String customerLocation, double maxDistanceInKm) {
        // Check if the customerLocation is not null and has the required format
        if (customerLocation == null || !customerLocation.contains(",")) {
            throw new IllegalArgumentException("Invalid customer location format");
        }

        String[] coordinates = customerLocation.split(",");
        if (coordinates.length != 2) {
            throw new IllegalArgumentException("Customer location should have both latitude and longitude");
        }

        double customerLatitude;
        double customerLongitude;
        try {
            customerLatitude = Double.parseDouble(coordinates[0].trim());
            customerLongitude = Double.parseDouble(coordinates[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid latitude or longitude format in customer location", e);
        }

        List<Driver> allDrivers = driverRepository.findAll(); // Retrieve all drivers

        List<Driver> nearbyDrivers = new ArrayList<>();
        for (Driver driver : allDrivers) {
            if (driver.getLatitude() == null || driver.getLongitude() == null) {
                continue;
            }

            double driverLatitude;
            double driverLongitude;
            try {
                driverLatitude = Double.parseDouble(driver.getLatitude().trim());
                driverLongitude = Double.parseDouble(driver.getLongitude().trim());
            } catch (NumberFormatException e) {
                continue;
            }

            double driverDistance = calculateDistance(customerLatitude, customerLongitude, driverLatitude, driverLongitude);

            if (driverDistance <= maxDistanceInKm) {
                nearbyDrivers.add(driver);
            }
        }
        return nearbyDrivers;
    }

    // Method to select the best driver based on criteria
    public Driver selectBestDriver(List<Driver> drivers, String customerLocation) {
        if (drivers.isEmpty()) {
            return null;
        }
        String[] coordinates = customerLocation.split(",");
        double customerLatitude = Double.parseDouble(coordinates[0]);
        double customerLongitude = Double.parseDouble(coordinates[1]);

        // Calculate driver scores based on distance, availability, rating.
        Map<Driver, Double> driverScores = new HashMap<>();
        for (Driver driver : drivers) {
            double distance = calculateDistance(customerLatitude, customerLongitude, Double.parseDouble(driver.getLatitude()),
                    Double.parseDouble(driver.getLongitude()));
            double availabilityScore = driver.getStatus() == AVAILABLE ? 1.0 : 0.0;
            double ratingScore = driver.getAverageRating() / FeedbackRating.values().length;

            // Calculate the overall score for the driver
            // Multiplying by 0.4 indicates that availability contributes to 40% of the final score
            double score = distance * 0.4 + availabilityScore * 0.4 + ratingScore * 0.2;
            System.out.println(driver + " score: " + score);
            driverScores.put(driver, score);
        }

        // Find the driver with the highest score
        return Collections.min(driverScores.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

}

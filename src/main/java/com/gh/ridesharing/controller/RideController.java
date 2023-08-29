package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Booking;
import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.enums.AvailabilityStatus;
import com.gh.ridesharing.enums.RideStatus;
import com.gh.ridesharing.service.CustomerService;
import com.gh.ridesharing.service.DriverService;
import com.gh.ridesharing.service.RideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ride-requests")
@Slf4j
public class RideController {
    private final RideService rideService;
    private final DriverService driverService;
    private final CustomerService customerService;

    @Value("${maximum.distance.location}")
    private String maximumDistanceLocation;

    // Constructor injection for services
    public RideController(RideService rideService, DriverService driverService, CustomerService customerService) {
        this.rideService = rideService;
        this.driverService = driverService;
        this.customerService = customerService;
    }

    // Endpoint to create a new ride
    @PostMapping("/{customerId}/create-ride")
    public ResponseEntity<String> createRide(@PathVariable Long customerId, @RequestBody Booking booking) {
        // Create a new Ride object and set the necessary details
        Ride ride = new Ride();
        // Assuming you have a method to get the current customer
        Customer customer = customerService.getCustomerById(customerId);
        ride.setCustomer(customer);
        ride.setPickUpLocation(booking.getPickupLocation());
        ride.setDropOffLocation(booking.getDropoffLocation());
        ride.setStatus(RideStatus.PENDING); // Set the initial status
        ride.setType(booking.getType());

        // Save the ride to the database
        rideService.createRide(ride);

        return ResponseEntity.ok("Ride created.");
    }

    // Endpoint to find the best driver for a ride
    @GetMapping("/find-best-driver")
    public ResponseEntity<Driver> findBestDriverForRide(@RequestParam Long rideId) {
        // Retrieve the created ride by ID
        Optional<Ride> rideOptional = Optional.ofNullable(rideService.getRideById(rideId));

        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            // Retrieve nearby drivers based on ride's pickup location
            List<Driver> nearbyDrivers = rideService.findNearbyDrivers(ride.getPickUpLocation(), Double.parseDouble(maximumDistanceLocation));

            // Select the best driver based on defined criteria
            Driver bestDriver = rideService.selectBestDriver(nearbyDrivers, ride.getPickUpLocation());

            if (bestDriver != null) {
                // Update ride with selected driver
                ride.setDriver(bestDriver);
                ride.setStatus(RideStatus.ACCEPTED);
                rideService.updateRide(ride);

                return ResponseEntity.ok(bestDriver);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint for a driver to accept a booking
    @PostMapping("/accept-booking/{driverId}")
    public ResponseEntity<String> acceptBooking(@PathVariable Long driverId) {
        // Retrieve the driver by ID
        Optional<Driver> driverOptional = driverService.getById(driverId);

        if (driverOptional.isPresent()) {
            Driver driver = driverOptional.get();
            // Update driver's status to 'UNAVAILABLE'
            driver.setStatus(AvailabilityStatus.UNAVAILABLE);
            driverService.update(driverId, driver);

            // Return a success message
            return ResponseEntity.ok("Driver accepted for Ride.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * API endpoint to rate a completed ride.
     *
     * @param rideId  The ID of the ride to rate.
     * @param rating  The rating value provided by the user.
     * @return ResponseEntity containing the updated ride or an error message.
     */
    @PutMapping("/{rideId}/rate")
    public ResponseEntity<Ride> rateRide(
            @PathVariable Long rideId,
            @RequestParam int rating) {
        try {
            // Call the rateRideRequest method from the service to rate the ride
            Ride ratedRide = rideService.rateRideRequest(rideId, rating);
            return ResponseEntity.ok(ratedRide); // Return the updated ride
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}


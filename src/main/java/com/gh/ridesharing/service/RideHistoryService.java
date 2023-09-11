package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideHistoryService {

    private final RideRepository rideRepository;

    // Constructor injection for the repository
    public RideHistoryService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    /**
     * Get the ride history for a specific customer.
     *
     * @param customer The customer for whom to retrieve the ride history.
     * @return List of rides in descending order of start time for the customer.
     */
    public List<Ride> getRideHistoryForCustomer(Customer customer) {
        return rideRepository.findByCustomerOrderByStartTimeDesc(customer);
    }

    /**
     * Get the ride history for a specific driver.
     *
     * @param driver The driver for whom to retrieve the ride history.
     * @return List of rides in descending order of start time for the driver.
     */
    public List<Ride> getRideHistoryForDriver(Driver driver) {
        return rideRepository.findByDriverOrderByStartTimeDesc(driver);
    }
    public List<Ride> getRidesByDriverId(Long driverId) {
        return rideRepository.findByDriverId(driverId);
    }
}

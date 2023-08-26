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

    public RideHistoryService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public List<Ride> getRideHistoryForCustomer(Customer customer) {
        return rideRepository.findByCustomerOrderByStartTimeDesc(customer);
    }

    public List<Ride> getRideHistoryForDriver(Driver driver) {
        return rideRepository.findByDriverOrderByStartTimeDesc(driver);
    }
}

package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.repository.RideRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideHistoryServiceTest {

    @Mock
    private RideRepository rideRepository;

    @InjectMocks
    private RideHistoryService rideHistoryService;

    @Test
    void testGetRideHistoryForCustomer() {
        Customer customer = new Customer(); // Assume a basic customer object; can be expanded as needed

        Ride ride1 = new Ride();
        Ride ride2 = new Ride();
        List<Ride> expectedRides = Arrays.asList(ride1, ride2);

        when(rideRepository.findByCustomerOrderByStartTimeDesc(customer)).thenReturn(expectedRides);

        List<Ride> resultRides = rideHistoryService.getRideHistoryForCustomer(customer);

        assertEquals(expectedRides.size(), resultRides.size());
        verify(rideRepository).findByCustomerOrderByStartTimeDesc(customer);
    }

    @Test
    void testGetRideHistoryForDriver() {
        Driver driver = new Driver(); // Assume a basic driver object; can be expanded as needed

        Ride ride1 = new Ride();
        Ride ride2 = new Ride();
        List<Ride> expectedRides = Arrays.asList(ride1, ride2);

        when(rideRepository.findByDriverOrderByStartTimeDesc(driver)).thenReturn(expectedRides);

        List<Ride> resultRides = rideHistoryService.getRideHistoryForDriver(driver);

        assertEquals(expectedRides.size(), resultRides.size());
        verify(rideRepository).findByDriverOrderByStartTimeDesc(driver);
    }
}

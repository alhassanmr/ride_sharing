package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.enums.FeedbackRating;
import com.gh.ridesharing.enums.RideStatus;
import com.gh.ridesharing.repository.DriverRepository;
import com.gh.ridesharing.repository.RideRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gh.ridesharing.enums.AvailabilityStatus.AVAILABLE;
import static com.gh.ridesharing.enums.AvailabilityStatus.UNAVAILABLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private RideService rideService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        rideRepository = mock(RideRepository.class);
//        driverRepository = mock(DriverRepository.class);
//        driverService = mock(DriverService.class);
//        rideService = new RideService(rideRepository,driverService, driverRepository);
//    }

    @Test
    void createRide_validRide() {
        Ride ride = new Ride();
        when(rideRepository.save(ride)).thenReturn(ride);
        assertEquals(ride, rideService.createRide(ride));
    }

    @Test
    void getRideById_existingId() {
        Ride ride = new Ride();
        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
        assertEquals(ride, rideService.getRideById(1L));
    }

    @Test
    void getRideById_nonExistingId() {
        when(rideRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> rideService.getRideById(1L));
    }

    @Test
    void deleteRideRequest_validRequest() {
        doNothing().when(rideRepository).deleteById(1L);
        rideService.deleteRideRequest(1L);
        verify(rideRepository, times(1)).deleteById(1L);
    }

    @Test
    void rateRideRequest_notCompletedRide() {
        Ride ride = new Ride();
        ride.setStatus(RideStatus.IN_PROGRESS);
        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));

        assertThrows(IllegalStateException.class, () -> rideService.rateRideRequest(1L, 5));
    }

    @Test
    void rateRideRequest_alreadyRatedRide() {
        Ride ride = new Ride();
        ride.setStatus(RideStatus.COMPLETED);
        ride.setRating(4);
        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));

        assertThrows(IllegalStateException.class, () -> rideService.rateRideRequest(1L, 5));
    }

    @Test
    void findNearbyDrivers_invalidCustomerLocation() {
        assertThrows(IllegalArgumentException.class, () -> rideService.findNearbyDrivers(null, 5));
        assertThrows(IllegalArgumentException.class, () -> rideService.findNearbyDrivers("invalid", 5));
    }

    @Test
    void selectBestDriver_noDrivers() {
        assertTrue(rideService.selectBestDriver(Collections.emptyList(), "12.34,56.78") == null);
    }

    @Test
    void updateRide_validRide() {
        Ride updatedRide = new Ride();
        updatedRide.setId(1L);
        updatedRide.setStatus(RideStatus.COMPLETED);

        // Mock behavior for the save method in rideRepository
        when(rideRepository.save(updatedRide)).thenReturn(updatedRide);

        // Call the updateRide method and assert that it returns the expected ride
        Ride returnedRide = rideService.updateRide(updatedRide);
        assertEquals(updatedRide, returnedRide);

        // Verify that the save method was called once
        verify(rideRepository, times(1)).save(updatedRide);
    }

//    @Test
//    void rateRideRequest_rideNotCompleted_shouldThrowException() {
//        Ride ride = new Ride();
//        ride.setId(1L);
//        ride.setStatus(RideStatus.IN_PROGRESS);  // Assuming there's an IN_PROGRESS status
//
//        // Mock behavior for findById in rideRepository
//        when(rideRepository.findById(1L)).thenReturn(java.util.Optional.of(ride));
//
//        // Assert that the exception is thrown
//        assertThrows(IllegalStateException.class, () -> rideService.rateRideRequest(1L, 5),
//                "Ride request is not completed.");
//    }

    @Test
    void rateRideRequest_rideAlreadyRated_shouldThrowException() {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setStatus(RideStatus.COMPLETED);
        ride.setRating(5);

        // Mock behavior for findById in rideRepository
        when(rideRepository.findById(1L)).thenReturn(java.util.Optional.of(ride));

        // Assert that the exception is thrown
        assertThrows(IllegalStateException.class, () -> rideService.rateRideRequest(1L, 4),
                "Ride request has already been rated.");
    }

    @Test
    void rateRideRequest_rateSuccessfully() {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setStatus(RideStatus.COMPLETED);

        when(rideRepository.findById(1L)).thenReturn(java.util.Optional.of(ride));
        when(rideRepository.save(any())).thenReturn(ride);

        Ride result = rideService.rateRideRequest(1L, 5);

        assertEquals(5, result.getRating());
    }

    @Test
    void rateRideRequest_withDriver_updateDriverRatingSuccessfully() {
        Ride ride = new Ride();
        ride.setId(1L);
        ride.setStatus(RideStatus.COMPLETED);

        Driver driver = new Driver();
        driver.setId(2L);
        driver.setRating(4);
        driver.setTotalRides(1);

        ride.setDriver(driver);

        when(rideRepository.findById(1L)).thenReturn(java.util.Optional.of(ride));
        when(rideRepository.save(any())).thenReturn(ride);

        Ride result = rideService.rateRideRequest(1L, 5);

        verify(driverService).update(eq(2L), any());
    }

    @Test
    void calculateDistance_validCoordinates_distanceReturned() {
        double lat1 = 40.7128;
        double lon1 = -74.0060;
        double lat2 = 34.0522;
        double lon2 = -118.2437;

        double distance = rideService.calculateDistance(lat1, lon1, lat2, lon2);

        assertTrue(distance > 0);
    }

    @Test
    void findNearbyDrivers_invalidLocationFormat_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> rideService.findNearbyDrivers("INVALID_FORMAT", 10));
    }

    @Test
    void findNearbyDrivers_validLocation_returnsNearbyDrivers() {
        List<Driver> drivers = new ArrayList<>();
        Driver driver1 = new Driver();
        driver1.setLatitude("35.0");
        driver1.setLongitude("-75.0");

        Driver driver2 = new Driver();
        driver2.setLatitude("36.0");
        driver2.setLongitude("-76.0");

        drivers.add(driver1);
        drivers.add(driver2);

        when(driverRepository.findAll()).thenReturn(drivers);

        List<Driver> result = rideService.findNearbyDrivers("35.5,-75.5", 100);

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void findNearbyDrivers_missingLatOrLong_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> rideService.findNearbyDrivers("35.5,", 10));
        assertThrows(IllegalArgumentException.class, () -> rideService.findNearbyDrivers(",75.5", 10));
        assertThrows(IllegalArgumentException.class, () -> rideService.findNearbyDrivers("35.5", 10));
    }

    @Test
    void findNearbyDrivers_invalidLatOrLong_throwsException() {
        RideService rideService = new RideService(rideRepository, driverService, driverRepository);

        assertThrows(IllegalArgumentException.class, () -> rideService.findNearbyDrivers("invalid,75.5", 10));
        assertThrows(IllegalArgumentException.class, () -> rideService.findNearbyDrivers("35.5,invalid", 10));
    }

    @Test
    void findNearbyDrivers_skipDriversWithNullLatOrLong() {
        // Given a list of drivers, one with a null latitude
        Driver driver1 = new Driver();
        driver1.setLatitude(null);
        driver1.setLongitude("75.5");

        Driver driver2 = new Driver();
        driver2.setLatitude("35.5");
        driver2.setLongitude("75.5");

        when(driverRepository.findAll()).thenReturn(Arrays.asList(driver1, driver2));

        // When calling findNearbyDrivers
        List<Driver> result = rideService.findNearbyDrivers("35.5,75.5", 10);

        // Then the resulting list should not contain driver1
        assertTrue(result.contains(driver2));
        assertFalse(result.contains(driver1));

    }

    @Test
    void findNearbyDrivers_skipDriversWithInvalidLatOrLongFormat() {
        Driver driver1 = new Driver();
        driver1.setLatitude("invalid_latitude");
        driver1.setLongitude("75.5");

        Driver driver2 = new Driver();
        driver2.setLatitude("35.5");
        driver2.setLongitude("75.5");

        when(driverRepository.findAll()).thenReturn(Arrays.asList(driver1, driver2));

        List<Driver> result = rideService.findNearbyDrivers("35.5,75.5", 10);

        assertTrue(result.contains(driver2));
        assertFalse(result.contains(driver1));
    }

    @Test
    void selectBestDriver_selectsBasedOnCriteria() {
        // Sample drivers
        Driver nearAvailableHighRatedDriver = new Driver();
        nearAvailableHighRatedDriver.setLatitude("35.1");
        nearAvailableHighRatedDriver.setLongitude("75.1");
        nearAvailableHighRatedDriver.setStatus(AVAILABLE);
        nearAvailableHighRatedDriver.setAverageRating(5);

        Driver farAvailableHighRatedDriver = new Driver();
        farAvailableHighRatedDriver.setLatitude("40.1");
        farAvailableHighRatedDriver.setLongitude("80.1");
        farAvailableHighRatedDriver.setStatus(AVAILABLE);
        farAvailableHighRatedDriver.setAverageRating(5);

        Driver nearUnavailableHighRatedDriver = new Driver();
        nearUnavailableHighRatedDriver.setLatitude("35.1");
        nearUnavailableHighRatedDriver.setLongitude("75.1");
        nearUnavailableHighRatedDriver.setStatus(AVAILABLE);
        nearUnavailableHighRatedDriver.setAverageRating(5);

        List<Driver> drivers = Arrays.asList(nearAvailableHighRatedDriver, farAvailableHighRatedDriver, nearUnavailableHighRatedDriver);

        // Call the method
        Driver bestDriver = rideService.selectBestDriver(drivers, "35.0,75.0");

        // Assert that the nearest available high-rated driver is selected
        assertEquals(nearAvailableHighRatedDriver, bestDriver, "Did not select the expected best driver.");
    }

}

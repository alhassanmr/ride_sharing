package com.gh.ridesharing.controller;

import com.gh.ridesharing.controller.RideController;
import com.gh.ridesharing.entity.Booking;
import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.enums.AvailabilityStatus;
import com.gh.ridesharing.service.CustomerService;
import com.gh.ridesharing.service.DriverService;
import com.gh.ridesharing.service.RideService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gh.ridesharing.enums.RideType.REGULAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"maximum.distance.location=100"})
public class RideControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RideService rideService;

    @Mock
    private DriverService driverService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private RideController rideController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(rideController).build();
    }

    @Test
    public void testRateRide_Success() {
        long rideId = 1L;
        int rating = 4;

        Ride ratedRide = new Ride();
        ratedRide.setId(rideId);
        ratedRide.setRating(rating);

        when(rideService.rateRideRequest(rideId, rating)).thenReturn(ratedRide);

        ResponseEntity<Ride> responseEntity = rideController.rateRide(rideId, rating);

        assertNotNull(responseEntity);
        assertEquals(ratedRide, responseEntity.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());

        verify(rideService, times(1)).rateRideRequest(rideId, rating);
    }

    @Test
    public void testRateRide_Failure() {
        long rideId = 1L;
        int rating = 4;

        when(rideService.rateRideRequest(rideId, rating)).thenThrow(new IllegalStateException());

        ResponseEntity<Ride> responseEntity = rideController.rateRide(rideId, rating);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(ResponseEntity.badRequest().body(null).getStatusCode(), responseEntity.getStatusCode());

        verify(rideService, times(1)).rateRideRequest(rideId, rating);
    }
    @Test
    public void testAcceptBooking_DriverExists() {
        long driverId = 1L;

        Driver mockDriver = new Driver();
        mockDriver.setId(driverId);

        when(driverService.getById(driverId)).thenReturn(Optional.of(mockDriver));

        ResponseEntity<String> responseEntity = rideController.acceptBooking(driverId);

        assertNotNull(responseEntity);
        assertEquals("Driver accepted for Ride.", responseEntity.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());

        verify(driverService, times(1)).getById(driverId);
        verify(driverService, times(1)).update(driverId, mockDriver);
    }

    @Test
    public void testAcceptBooking_DriverNotFound() {
        long driverId = 1L;

        when(driverService.getById(driverId)).thenReturn(Optional.empty());

        ResponseEntity<String> responseEntity = rideController.acceptBooking(driverId);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), responseEntity.getStatusCode());

        verify(driverService, times(1)).getById(driverId);
        verify(driverService, times(0)).update(anyLong(), any(Driver.class));
    }

    @Test
    public void testCreateRide_Success() throws Exception {
        Booking mockBooking = new Booking();
        mockBooking.setPickupLocation("{\"lat\": 40.712776, \"lng\": -74.005974}");
        mockBooking.setDropoffLocation("{\"lat\": 40.758896, \"lng\": -73.985130}");
        mockBooking.setType(REGULAR);

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);

        Ride mockRide = new Ride();

        Driver mockDriver = new Driver();
        mockDriver.setId(2L);

        ReflectionTestUtils.setField(rideController, "maximumDistanceLocation", "100");

        when(rideService.findNearbyDrivers(anyString(), anyDouble())).thenReturn(List.of(mockDriver));
        when(rideService.selectBestDriver(any(), any())).thenReturn(mockDriver);
        when(rideService.createRide(any(Ride.class))).thenReturn(mockRide);

        ResponseEntity<Driver> responseEntity = rideController.createRide(1L, mockBooking);

        assertNotNull(responseEntity);
        assertEquals(mockDriver, responseEntity.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());

        verify(rideService, times(1)).findNearbyDrivers(anyString(), anyDouble());
        verify(rideService, times(1)).selectBestDriver(any(), any());
        verify(rideService, times(1)).createRide(any(Ride.class));
    }

    @Test
    public void testCreateRide_NoAvailableDriver() throws Exception {
        Booking mockBooking = new Booking();
        mockBooking.setPickupLocation("{\"lat\": 40.712776, \"lng\": -74.005974}");
        mockBooking.setDropoffLocation("{\"lat\": 40.758896, \"lng\": -73.985130}");
        mockBooking.setType(REGULAR);

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);

        ReflectionTestUtils.setField(rideController, "maximumDistanceLocation", "100");
        when(rideService.findNearbyDrivers(anyString(), anyDouble())).thenReturn(List.of());
        when(rideService.selectBestDriver(any(), any())).thenReturn(null);

        ResponseEntity<Driver> responseEntity = rideController.createRide(1L, mockBooking);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), responseEntity.getStatusCode());

        verify(rideService, times(1)).findNearbyDrivers(anyString(), anyDouble());
        verify(rideService, times(1)).selectBestDriver(any(), any());
        verify(rideService, times(0)).createRide(any(Ride.class));
    }

    @Test
    public void testCreateRide_JsonProcessingException() throws Exception {
        Booking mockBooking = new Booking();
        mockBooking.setPickupLocation("{\"lat\": 40.712776, \"lng\": -74.005974}");
        mockBooking.setDropoffLocation("{\"lat\": 40.758896, \"lng\": -73.985130}");
        mockBooking.setType(REGULAR);

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setUsername("testUser");
        mockCustomer.setEmail("test@example.com");

        // Set up the @Value property for testing
        ReflectionTestUtils.setField(rideController, "maximumDistanceLocation", "100");

        // Set up your mock service responses
        when(customerService.getCustomerById(anyLong())).thenReturn(mockCustomer);

        ObjectMapper objectMapper = new ObjectMapper();
        String mockBookingJson = objectMapper.writeValueAsString(mockBooking);

        mockMvc.perform(post("/api/ride-requests/1/create-ride")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockBookingJson))
                .andExpect(status().isBadRequest());

        verify(customerService, times(1)).getCustomerById(anyLong());
    }

    @Test
    @DisplayName("Successfully confirm ride by customer")
    public void testConfirmRideByCustomer_Success() {
        long rideId = 1L;
        Ride confirmedRide = new Ride();
        confirmedRide.setId(rideId);

        when(rideService.confirmRideByCustomer(rideId)).thenReturn(confirmedRide);

        ResponseEntity<Ride> responseEntity = rideController.confirmRideByCustomer(rideId);

        assertNotNull(responseEntity);
        assertEquals(confirmedRide, responseEntity.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());

        verify(rideService, times(1)).confirmRideByCustomer(rideId);
    }

    @Test
    @DisplayName("Fail to confirm ride by customer due to ride not found")
    public void testConfirmRideByCustomer_RideNotFound() {
        long rideId = 1L;

        when(rideService.confirmRideByCustomer(rideId)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Ride> responseEntity = rideController.confirmRideByCustomer(rideId);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), responseEntity.getStatusCode());

        verify(rideService, times(1)).confirmRideByCustomer(rideId);
    }

    @Test
    @DisplayName("Fail to confirm ride by customer due to illegal state")
    public void testConfirmRideByCustomer_IllegalState() {
        long rideId = 1L;

        when(rideService.confirmRideByCustomer(rideId)).thenThrow(new IllegalStateException());

        ResponseEntity<Ride> responseEntity = rideController.confirmRideByCustomer(rideId);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), responseEntity.getStatusCode());

        verify(rideService, times(1)).confirmRideByCustomer(rideId);
    }

    @Test
    @DisplayName("Successfully retrieve rides for a customer")
    public void testGetRidesForCustomer_Success() {
        long customerId = 1L;
        List<Ride> rides = Arrays.asList(new Ride(), new Ride());

        when(rideService.getRidesByCustomerId(customerId)).thenReturn(rides);

        ResponseEntity<List<Ride>> responseEntity = rideController.getRidesForCustomer(customerId);

        assertNotNull(responseEntity);
        assertEquals(rides, responseEntity.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("No rides found for the customer")
    public void testGetRidesForCustomer_NoRidesFound() {
        long customerId = 1L;

        when(rideService.getRidesByCustomerId(customerId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Ride>> responseEntity = rideController.getRidesForCustomer(customerId);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Successfully confirm ride by driver")
    public void testConfirmRideByDriver_Success() {
        long rideId = 1L;
        Ride confirmedRide = new Ride();
        confirmedRide.setId(rideId);

        when(rideService.confirmRideByDriver(rideId)).thenReturn(confirmedRide);

        ResponseEntity<Ride> responseEntity = rideController.confirmRideByDriver(rideId);

        assertNotNull(responseEntity);
        assertEquals(confirmedRide, responseEntity.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Fail to confirm ride by driver due to ride not found")
    public void testConfirmRideByDriver_RideNotFound() {
        long rideId = 1L;

        when(rideService.confirmRideByDriver(rideId)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Ride> responseEntity = rideController.confirmRideByDriver(rideId);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Fail to confirm ride by driver due to illegal state")
    public void testConfirmRideByDriver_IllegalState() {
        long rideId = 1L;

        when(rideService.confirmRideByDriver(rideId)).thenThrow(new IllegalStateException());

        ResponseEntity<Ride> responseEntity = rideController.confirmRideByDriver(rideId);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Ride exists and a best driver is found")
    public void testFindBestDriverForRide_Success() {
        long rideId = 1L;

        Ride mockRide = new Ride();
        mockRide.setId(rideId);
        mockRide.setPickUpLocation("Location");

        Driver mockDriver = new Driver();
        mockDriver.setId(2L);

        // Set up the maximumDistanceLocation for testing
        ReflectionTestUtils.setField(rideController, "maximumDistanceLocation", "100");

        when(rideService.getRideById(rideId)).thenReturn(mockRide);
        when(rideService.findNearbyDrivers(mockRide.getPickUpLocation(), Double.parseDouble("100")))
                .thenReturn(Arrays.asList(mockDriver));
        when(rideService.selectBestDriver(any(), any())).thenReturn(mockDriver);
        when(rideService.updateRide(any())).thenReturn(mockRide);

        ResponseEntity<Driver> responseEntity = rideController.findBestDriverForRide(rideId);

        assertNotNull(responseEntity);
        assertEquals(mockDriver, responseEntity.getBody());
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Ride exists but no best driver is found")
    public void testFindBestDriverForRide_NoDriverFound() {
        long rideId = 1L;

        Ride mockRide = new Ride();
        mockRide.setId(rideId);
        mockRide.setPickUpLocation("Location");
        // Set up the maximumDistanceLocation for testing
        ReflectionTestUtils.setField(rideController, "maximumDistanceLocation", "100");

        when(rideService.getRideById(rideId)).thenReturn(mockRide);
        when(rideService.findNearbyDrivers(mockRide.getPickUpLocation(), Double.parseDouble("100")))
                .thenReturn(Collections.emptyList());
        when(rideService.selectBestDriver(any(), any())).thenReturn(null);

        ResponseEntity<Driver> responseEntity = rideController.findBestDriverForRide(rideId);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Ride does not exist")
    public void testFindBestDriverForRide_RideNotFound() {
        long rideId = 1L;

        when(rideService.getRideById(rideId)).thenReturn(null);

        ResponseEntity<Driver> responseEntity = rideController.findBestDriverForRide(rideId);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), responseEntity.getStatusCode());
    }
}

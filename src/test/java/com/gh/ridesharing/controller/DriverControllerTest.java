package com.gh.ridesharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.entity.User;
import com.gh.ridesharing.enums.AvailabilityStatus;
import com.gh.ridesharing.service.DriverService;
import com.gh.ridesharing.service.RideHistoryService;
import com.gh.ridesharing.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DriverControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DriverService driverService;

    @Mock
    private RideHistoryService rideHistoryService;

    @Mock
    private UserService userService;

    @InjectMocks
    private DriverController driverController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(driverController).build();
    }

    @Test
    public void testGetDriverById() throws Exception {
        Driver mockDriver = new Driver();
        mockDriver.setId(1L);

        when(driverService.getById(1L)).thenReturn(Optional.of(mockDriver));

        mockMvc.perform(get("/api/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(driverService, times(1)).getById(1L);
    }

    @Test
    public void testGetDriverByIdNotFound() throws Exception {
        when(driverService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(driverService, times(1)).getById(1L);
    }

    @Test
    public void testCreateDriver() throws Exception {
        Driver mockDriver = new Driver();

        // This is a dummy request body for the driver. Modify as needed to match your Driver class structure.
        String driverJson = "{ \"name\": \"John Doe\", \"licenseNumber\": \"XYZ12345\" }";

        when(driverService.create(any(Driver.class))).thenReturn(mockDriver);

        mockMvc.perform(post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(driverJson))
                .andExpect(status().isOk());

        verify(driverService, times(1)).create(any(Driver.class));
    }

    @Test
    public void testUpdateDriver() throws Exception {
        Driver mockDriver = new Driver();
        mockDriver.setId(1L);

        // Convert mockDriver to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String driverJson = objectMapper.writeValueAsString(mockDriver);

        when(driverService.update(1L, mockDriver)).thenReturn(mockDriver);

        mockMvc.perform(put("/api/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(driverJson))
                .andExpect(status().isOk());

        verify(driverService, times(1)).update(eq(1L), any(Driver.class));
    }


    @Test
    public void testDeleteDriver() throws Exception {
        doNothing().when(driverService).deleteById(1L);

        mockMvc.perform(delete("/api/drivers/1"))
                .andExpect(status().isNoContent());

        verify(driverService, times(1)).deleteById(1L);
    }

    @Test
    public void testSetDriverAvailability() throws Exception {
        Driver mockDriver = new Driver();
        mockDriver.setId(1L);

        when(driverService.setAvailability(1L, AvailabilityStatus.AVAILABLE)).thenReturn(mockDriver);

        mockMvc.perform(put("/api/drivers/1/availability")
                        .param("status", "AVAILABLE"))
                .andExpect(status().isOk());

        verify(driverService, times(1)).setAvailability(1L, AvailabilityStatus.AVAILABLE);
    }

    @Test
    public void testGetAllDrivers() throws Exception {
        List<Driver> mockDrivers = new ArrayList<>();
        mockDrivers.add(new Driver());

        when(driverService.getAllDrivers()).thenReturn(mockDrivers);

        mockMvc.perform(get("/api/drivers"))
                .andExpect(status().isOk());

        verify(driverService, times(1)).getAllDrivers();
    }

    @Test
    public void testGetAllActiveDrivers() throws Exception {
        List<User> mockDrivers = new ArrayList<>();
        mockDrivers.add(new User());

        when(userService.getAllActiveDrivers()).thenReturn(mockDrivers);

        mockMvc.perform(get("/api/drivers/activealldrivers"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllActiveDrivers();
    }


    @Test
    public void testGetRidesByDriverIdWithRides() throws Exception {
        Long driverId = 1L;
        List<Ride> mockRides = new ArrayList<>();
        Ride mockRide = new Ride(); // You can set any fields if necessary.
        mockRides.add(mockRide);

        when(rideHistoryService.getRidesByDriverId(driverId)).thenReturn(mockRides);

        mockMvc.perform(get("/api/drivers/" + driverId + "/rides")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(rideHistoryService, times(1)).getRidesByDriverId(driverId);
    }

    @Test
    public void testGetRidesByDriverIdWithoutRides() throws Exception {
        Long driverId = 1L;
        List<Ride> mockRides = new ArrayList<>();

        when(rideHistoryService.getRidesByDriverId(driverId)).thenReturn(mockRides);

        mockMvc.perform(get("/api/drivers/" + driverId + "/rides")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(rideHistoryService, times(1)).getRidesByDriverId(driverId);
    }
}

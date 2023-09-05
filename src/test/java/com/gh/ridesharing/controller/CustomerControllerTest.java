package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.service.CustomerService;
import com.gh.ridesharing.service.RideHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @Mock
    private RideHistoryService rideHistoryService;

    @InjectMocks
    private CustomerController customerController;

    // Sample data for tests
    private Customer sampleCustomer;
    private Ride sampleRide;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        sampleCustomer = new Customer();
        sampleRide = new Ride();
    }

    @Test
    public void testCreateCustomer() throws Exception {
        when(customerService.create(any())).thenReturn(sampleCustomer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(customerService, times(1)).create(any());
    }

    @Test
    public void testGetCustomerById() throws Exception {
        when(customerService.getById(anyLong())).thenReturn(Optional.of(sampleCustomer));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(customerService, times(1)).getById(1L);
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        when(customerService.update(anyLong(), any())).thenReturn(sampleCustomer);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(customerService, times(1)).update(1L, sampleCustomer);
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteById(anyLong());

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteById(1L);
    }

    @Test
    public void testGetRideHistoryForCustomer() throws Exception {
        when(customerService.getById(anyLong())).thenReturn(Optional.of(sampleCustomer));
        when(rideHistoryService.getRideHistoryForCustomer(sampleCustomer)).thenReturn(Arrays.asList(sampleRide));

        mockMvc.perform(get("/api/customers/1/ride-history"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(rideHistoryService, times(1)).getRideHistoryForCustomer(sampleCustomer);
    }

    @Test
    public void testGetRideHistoryForNonExistentCustomer() throws Exception {
        // Ensure the customerService mock returns an empty Optional, simulating the customer not being found
        when(customerService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/1/ride-history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Expected 404 Not Found response

        verify(customerService, times(1)).getById(1L);
        verify(rideHistoryService, times(0)).getRideHistoryForCustomer(any()); // ensure ride history is not fetched for non-existent customer
    }
}

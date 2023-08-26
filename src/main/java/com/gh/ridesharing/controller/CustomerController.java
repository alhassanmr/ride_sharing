package com.gh.ridesharing.controller;

import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.service.CustomerService;
import com.gh.ridesharing.service.RideHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;
    private final RideHistoryService rideHistoryService;

    public CustomerController(CustomerService customerService, RideHistoryService rideHistoryService) {
        this.customerService = customerService;
        this.rideHistoryService = rideHistoryService;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer newCustomer) {
        log.info("Request to create a new customer");
        Customer customer = customerService.create(newCustomer);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
        log.info("Request to get customer by ID: {}", customerId);
        Optional<Customer> customer = customerService.getById(customerId);
        return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long customerId, @RequestBody Customer updatedCustomer) {
        log.info("Request to update customer with ID: {}", customerId);
        Customer customer = customerService.update(customerId, updatedCustomer);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        log.info("Request to delete customer with ID: {}", customerId);
        customerService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{customerId}/ride-history")
    public ResponseEntity<List<Ride>> getRideHistoryForCustomer(@PathVariable Long customerId) {
        Customer customer = customerService.getById(customerId).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        List<Ride> rideHistory = rideHistoryService.getRideHistoryForCustomer(customer);
        return ResponseEntity.ok(rideHistory);
    }
}

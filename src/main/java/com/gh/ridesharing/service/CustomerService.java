package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Ride;
import com.gh.ridesharing.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class CustomerService extends BaseServiceImpl<Customer> {

    private final CustomerRepository customerRepository;

    // Constructor injection for the repository
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        super(customerRepository);
        this.customerRepository = customerRepository;
    }

    /**
     * Get a customer by ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return The retrieved customer.
     * @throws EntityNotFoundException if the customer is not found.
     */
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer with ID " + customerId + " not found."));
    }

}

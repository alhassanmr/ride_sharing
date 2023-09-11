package com.gh.ridesharing.repository;

import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByCustomerOrderByStartTimeDesc(Customer customer);
    List<Ride> findByDriverOrderByStartTimeDesc(Driver driver);
    List<Ride> findByCustomer_Id(Long customerId);
    List<Ride> findByDriverId(Long driverId);
}


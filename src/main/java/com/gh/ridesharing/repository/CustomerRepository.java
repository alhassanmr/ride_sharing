package com.gh.ridesharing.repository;

import com.gh.ridesharing.entity.Customer;
import com.gh.ridesharing.enums.RoleType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseEntityRepository<Customer> {

    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    List<Customer> findByUser_Role_RoleType(RoleType roleType);
    List<Customer> findByIsMember(boolean isMember);


    @Query("SELECT c FROM Customer c WHERE EXISTS (SELECT b FROM Booking b WHERE b.customer = c AND b.status = 'ACTIVE')")
    List<Customer> findCustomersWithActiveBookings();

    @Query("SELECT c FROM Customer c WHERE c.id IN (SELECT b.customer.id FROM Booking b GROUP BY b.customer.id HAVING COUNT(DISTINCT b.route) > :minFrequentRoutes)")
    List<Customer> findCustomersWithFrequentRoutes(@Param("minFrequentRoutes") int minFrequentRoutes);

}
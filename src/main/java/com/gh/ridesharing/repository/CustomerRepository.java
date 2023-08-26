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

}
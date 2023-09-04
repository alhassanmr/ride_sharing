package com.gh.ridesharing.repository;

import com.gh.ridesharing.entity.Driver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends BaseEntityRepository<Driver> {
//    @Query(value = "SELECT * FROM driver WHERE user_id = :userId", nativeQuery = true)
//    Optional<Driver> findByUserIdQuery(@Param("userId") Long userId);
}



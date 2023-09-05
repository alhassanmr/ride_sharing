package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.Driver;
import com.gh.ridesharing.enums.AvailabilityStatus;
import com.gh.ridesharing.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    @Test
    void testGetById() {
        Driver driver = new Driver();
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        Optional<Driver> result = driverService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(driver, result.get());
    }

    @Test
    void testSetAvailability() {
        Driver driver = new Driver();
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(driver)).thenReturn(driver);

        Driver result = driverService.setAvailability(1L, AvailabilityStatus.AVAILABLE);

        assertEquals(AvailabilityStatus.AVAILABLE, result.getStatus());
    }

    @Test
    void testSetAvailabilityForDriverNotFound() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            driverService.setAvailability(1L, AvailabilityStatus.AVAILABLE);
        });
    }

    @Test
    void testGetAllDrivers() {
        Driver driver1 = new Driver();
        Driver driver2 = new Driver();
        List<Driver> drivers = Arrays.asList(driver1, driver2);

        when(driverRepository.findAll()).thenReturn(drivers);

        List<Driver> result = driverService.getAllDrivers();

        assertEquals(2, result.size());
    }

    @Test
    void testFindByUserId() {
        Driver driver1 = new Driver();
        Driver driver2 = new Driver();
        List<Driver> drivers = Arrays.asList(driver1, driver2);

        when(driverRepository.findAll()).thenReturn(drivers);

        List<Driver> result = driverService.findByUserId(1L);

        assertEquals(2, result.size());
    }
}

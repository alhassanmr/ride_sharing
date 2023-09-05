package com.gh.ridesharing.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gh.ridesharing.entity.BaseEntity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BaseServiceImplTest {

    @Mock
    private JpaRepository<BaseEntity, Long> repository;

    @InjectMocks
    private BaseServiceImpl<BaseEntity> baseService;

    @Test
    void testUpdateEntityFound() {
        BaseEntity entity = mock(BaseEntity.class);
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(entity)).thenReturn(entity);

        BaseEntity result = baseService.update(1L, entity);

        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    void testUpdateEntityNotFound() {
        BaseEntity entity = mock(BaseEntity.class);
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> baseService.update(1L, entity));
    }

    @Test
    void testCreateEntity() {
        BaseEntity entity = mock(BaseEntity.class);
        when(repository.save(entity)).thenReturn(entity);

        BaseEntity result = baseService.create(entity);

        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    void testGetAll() {
        BaseEntity entity = mock(BaseEntity.class);
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));

        List<BaseEntity> entities = baseService.getAll();

        assertNotNull(entities);
        assertFalse(entities.isEmpty());
        assertEquals(entity, entities.get(0));
    }

    @Test
    void testGetByIdEntityFound() {
        BaseEntity entity = mock(BaseEntity.class);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<BaseEntity> result = baseService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void testGetByIdEntityNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<BaseEntity> result = baseService.getById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById() {
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> baseService.deleteById(1L));
    }
}


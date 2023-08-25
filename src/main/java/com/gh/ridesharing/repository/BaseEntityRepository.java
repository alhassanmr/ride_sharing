package com.gh.ridesharing.repository;

import com.gh.ridesharing.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Custom base repository interface extending JpaRepository.
 * This interface provides common methods for interacting with entities.
 *
 * @param <T> The entity type.
 */
@NoRepositoryBean
public interface BaseEntityRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

    /**
     * Find an entity by its ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return An Optional containing the found entity, or empty if not found.
     */
    Optional<T> findById(Long id);
}

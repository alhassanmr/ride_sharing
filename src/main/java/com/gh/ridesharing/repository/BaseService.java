package com.gh.ridesharing.repository;

import com.gh.ridesharing.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Base service interface providing common CRUD operations for entities.
 *
 * @param <T> The entity type.
 */
public interface BaseService<T extends BaseEntity> {

    /**
     * Update an entity by its ID.
     *
     * @param id           The ID of the entity to update.
     * @param updatedEntity The updated entity data.
     * @return The updated entity.
     */
    T update(Long id, T updatedEntity);

    /**
     * Create a new entity.
     *
     * @param entity The entity to create.
     * @return The created entity.
     */
    T create(T entity);

    /**
     * Get all entities.
     *
     * @return A list of all entities.
     */
    List<T> getAll();

    /**
     * Get an entity by its ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return An Optional containing the found entity, or empty if not found.
     */
    Optional<T> getById(Long id);

    /**
     * Delete an entity by its ID.
     *
     * @param id The ID of the entity to delete.
     */
    void deleteById(Long id);
}

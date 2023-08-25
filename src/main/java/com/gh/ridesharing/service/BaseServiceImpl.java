package com.gh.ridesharing.service;

import com.gh.ridesharing.entity.BaseEntity;
import com.gh.ridesharing.repository.BaseEntityRepository;
import com.gh.ridesharing.repository.BaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    private final JpaRepository<T, Long> repository;

    @Autowired
    public BaseServiceImpl(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    /**
     * Update an entity by ID.
     *
     * @param id           The ID of the entity to update.
     * @param updatedEntity The updated entity data.
     * @return The updated entity.
     * @throws EntityNotFoundException if the entity with the given ID doesn't exist.
     */
    @Override
    public T update(Long id, T updatedEntity) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Entity with ID " + id + " not found.");
        }

        updatedEntity.setId(id);  // Set the ID of the updated entity
        return repository.save(updatedEntity);
    }

    /**
     * Create a new entity.
     *
     * @param entity The entity to create.
     * @return The created entity.
     */
    @Override
    public T create(T entity) {
        return repository.save(entity);
    }

    /**
     * Get all entities.
     *
     * @return A list of all entities.
     */
    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    /**
     * Get an entity by ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return An Optional containing the found entity, or empty if not found.
     */
    @Override
    public Optional<T> getById(Long id) {
        return repository.findById(id);
    }

    /**
     * Delete an entity by ID.
     *
     * @param id The ID of the entity to delete.
     */
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

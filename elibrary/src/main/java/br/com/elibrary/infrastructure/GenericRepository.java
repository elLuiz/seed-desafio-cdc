package br.com.elibrary.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

/**
 * Represents basic operations performed in any repository throughout the system
 * @param <E> The type of the Entity
 * @param <I> The type of the identifier of the entity
 */
class GenericRepository<E, I> {
    @PersistenceContext
    protected EntityManager entityManager;
    private final Class<E> type;

    public GenericRepository(Class<E> type) {
        this.type = type;
    }

    public void add(E entity) {
        if (entity != null) {
            entityManager.persist(entity);
        }
    }

    public void update(E entity) {
        if (entity != null) {
            entityManager.merge(entity);
        }
    }

    public Optional<E> findById(I id) {
        return Optional.ofNullable(entityManager.find(type, id));
    }
}
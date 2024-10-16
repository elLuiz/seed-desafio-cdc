package br.com.elibrary.infrastructure;

import br.com.elibrary.model.category.Category;
import br.com.elibrary.service.category.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
class CategoryEntityManagerRepository implements CategoryRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void add(Category category) {
        entityManager.persist(category);
    }
}
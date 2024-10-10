package br.com.elibrary.infrastructure;

import br.com.elibrary.model.entity.Author;
import br.com.elibrary.service.AuthorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
class AuthorEntityManagerRepository implements AuthorRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void add(Author author) {
        entityManager.persist(author);
    }
}

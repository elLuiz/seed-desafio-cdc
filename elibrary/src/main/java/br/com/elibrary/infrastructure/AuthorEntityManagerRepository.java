package br.com.elibrary.infrastructure;

import br.com.elibrary.model.author.Author;
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

    @Override
    public boolean isUnique(String email) {
        Object result = entityManager.createNativeQuery("SELECT count(email) = 0 FROM {h-schema}tb_author WHERE UPPER(email)=UPPER(TRIM(:email))")
                .setParameter("email", email)
                .getSingleResult();
        return Boolean.TRUE.equals(result);
    }
}

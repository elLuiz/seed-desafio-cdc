package br.com.elibrary.infrastructure;

import br.com.elibrary.model.author.Author;
import br.com.elibrary.service.author.AuthorRepository;
import org.springframework.stereotype.Repository;

@Repository
class AuthorEntityManagerRepository extends GenericRepository<Author, Long> implements AuthorRepository {
    AuthorEntityManagerRepository() {
        super(Author.class);
    }

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

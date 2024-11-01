package br.com.elibrary.infrastructure;

import br.com.elibrary.model.author.Author;
import br.com.elibrary.service.author.AuthorRepository;
import br.com.elibrary.util.string.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class AuthorEntityManagerRepository extends GenericRepository<Author, Long> implements AuthorRepository {
    AuthorEntityManagerRepository() {
        super(Author.class);
    }

    @Override
    public boolean isUnique(String email) {
        Object result = entityManager.createNativeQuery("SELECT count(email) = 0 FROM {h-schema}tb_author WHERE UPPER(email)=UPPER(TRIM(:email))")
                .setParameter("email", email)
                .getSingleResult();
        return Boolean.TRUE.equals(result);
    }

    @Override
    public Optional<Author> findByEmail(String email) {
        if (StringUtils.isNotEmpty(email)) {
            Author author = entityManager.createQuery("SELECT author FROM Author author WHERE UPPER(TRIM(email))=UPPER(TRIM(:email))", Author.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.ofNullable(author);
        }
        return Optional.empty();
    }
}
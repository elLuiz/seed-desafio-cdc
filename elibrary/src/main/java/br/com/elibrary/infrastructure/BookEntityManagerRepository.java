package br.com.elibrary.infrastructure;

import br.com.elibrary.model.book.Book;
import br.com.elibrary.service.book.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class BookEntityManagerRepository extends GenericRepository<Book, Long> implements BookRepository {
    public BookEntityManagerRepository() {
        super(Book.class);
    }

    @Override
    public Optional<Book> findById(Long id) {
        Book book = entityManager.createQuery("SELECT b FROM Book b " +
                "JOIN FETCH b.author " +
                "WHERE b.id = :id", Book.class)
                .setParameter("id", id)
                .getSingleResult();
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> listAll() {
        return entityManager.createQuery("SELECT book FROM Book book " +
                "ORDER BY book.title ASC", Book.class).getResultList();
    }

    @Override
    public Optional<Book> findByISBN(String isbn) {
        Book book = entityManager.createQuery("SELECT book FROM Book book " +
                        "WHERE UPPER(TRIM(isbn))=UPPER(TRIM(:isbn))", Book.class)
                .setParameter("isbn", isbn)
                .getSingleResult();
        return Optional.ofNullable(book);
    }
}

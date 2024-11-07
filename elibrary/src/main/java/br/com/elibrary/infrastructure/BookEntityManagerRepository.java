package br.com.elibrary.infrastructure;

import br.com.elibrary.model.book.Book;
import br.com.elibrary.service.book.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class BookEntityManagerRepository extends GenericRepository<Book, Long> implements BookRepository {
    public BookEntityManagerRepository() {
        super(Book.class);
    }

    @Override
    public List<Book> listAll() {
        return entityManager.createQuery("SELECT book FROM Book book " +
                "ORDER BY book.title ASC", Book.class).getResultList();
    }
}

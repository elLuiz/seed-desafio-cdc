package br.com.elibrary.infrastructure;

import br.com.elibrary.model.book.Book;
import br.com.elibrary.service.book.BookRepository;
import org.springframework.stereotype.Repository;

@Repository
class BookEntityManagerRepository extends GenericRepository<Book, Long> implements BookRepository {
    public BookEntityManagerRepository() {
        super(Book.class);
    }
}

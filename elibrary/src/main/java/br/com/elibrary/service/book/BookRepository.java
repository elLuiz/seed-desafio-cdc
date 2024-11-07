package br.com.elibrary.service.book;

import br.com.elibrary.model.book.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    void add(Book book);

    List<Book> listAll();

    Optional<Book> findById(Long id);

    Optional<Book> findByISBN(String isbn);
}
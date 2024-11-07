package br.com.elibrary.service.book;

import br.com.elibrary.model.book.Book;

import java.util.List;

public interface BookRepository {
    void add(Book book);

    List<Book> listAll();
}
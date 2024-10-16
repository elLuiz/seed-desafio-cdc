package br.com.elibrary.service.author;

import br.com.elibrary.model.author.Author;

public interface AuthorRepository {
    void add(Author author);
    boolean isUnique(String email);
}
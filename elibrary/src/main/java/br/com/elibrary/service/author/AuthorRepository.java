package br.com.elibrary.service.author;

import br.com.elibrary.model.author.Author;

import java.util.Optional;

public interface AuthorRepository {
    void add(Author author);
    boolean isUnique(String email);
    Optional<Author> findById(Long id);
}
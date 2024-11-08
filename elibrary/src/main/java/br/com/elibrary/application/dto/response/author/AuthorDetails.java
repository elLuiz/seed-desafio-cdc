package br.com.elibrary.application.dto.response.author;

import br.com.elibrary.model.author.Author;

public record AuthorDetails(String authorName, String description) {
    public static AuthorDetails convert(Author author) {
        return new AuthorDetails(author.getName(), author.getDescription());
    }
}

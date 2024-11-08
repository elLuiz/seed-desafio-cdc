package br.com.elibrary.application.dto.response.author;

import br.com.elibrary.model.author.Author;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthorCreatedResponse {
    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;

    private AuthorCreatedResponse(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static AuthorCreatedResponse convert(Author author) {
        return new AuthorCreatedResponse(author.getId(), author.getName(), author.getCreatedAt());
    }
}
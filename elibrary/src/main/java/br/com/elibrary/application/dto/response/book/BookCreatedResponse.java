package br.com.elibrary.application.dto.response.book;

import br.com.elibrary.model.book.Book;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookCreatedResponse {
    private final Long id;
    private final String title;
    private final LocalDateTime publishAt;

    BookCreatedResponse(Long id, LocalDateTime publishAt, String title) {
        this.id = id;
        this.publishAt = publishAt;
        this.title = title;
    }

    public static BookCreatedResponse convert(Book book) {
        return new BookCreatedResponse(book.getId(), book.getPublishAt(), book.getTitle());
    }
}

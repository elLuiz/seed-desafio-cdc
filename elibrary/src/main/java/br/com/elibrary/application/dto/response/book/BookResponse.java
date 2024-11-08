package br.com.elibrary.application.dto.response.book;

import br.com.elibrary.model.book.Book;
import lombok.Getter;

@Getter
public class BookResponse {
    private Long id;
    private String title;

    private BookResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static BookResponse convert(Book book) {
        if (book != null) {
            return new BookResponse(book.getId(), book.getTitle());
        }
        throw new IllegalArgumentException("Book must not be null.");
    }
}

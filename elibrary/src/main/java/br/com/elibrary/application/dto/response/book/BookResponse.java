package br.com.elibrary.application.dto.response.book;

import br.com.elibrary.model.book.Book;
import br.com.elibrary.model.common.Money;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BookResponse {
    private Long id;
    private String title;
    private BigDecimal price;

    private BookResponse(Long id, String title, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public static BookResponse convert(Book book) {
        if (book != null) {
            return new BookResponse(book.getId(), book.getTitle(), Money.round(book.getPrice(), 2));
        }
        throw new IllegalArgumentException("Book must not be null.");
    }
}

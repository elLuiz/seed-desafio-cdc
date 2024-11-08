package br.com.elibrary.application.dto.response.book;

import br.com.elibrary.application.dto.response.author.AuthorDetails;
import br.com.elibrary.model.book.Book;
import br.com.elibrary.model.book.Money;
import lombok.Getter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Getter
public class BookDetailsResponse {
    private Long id;
    private String title;
    private String summary;
    private String tableOfContents;
    private BigDecimal price;
    private String isbn;
    private short pages;
    private String publishAt;
    private AuthorDetails authorDetails;

    public static BookDetailsResponse convert(Book book) {
        Assert.notNull(book, "book.must.not.be.null");
        BookDetailsResponse bookDetailsResponse = new BookDetailsResponse();
        bookDetailsResponse.id = book.getId();
        bookDetailsResponse.title = book.getTitle();
        bookDetailsResponse.summary = book.getSummary();
        bookDetailsResponse.tableOfContents = book.getTableOfContents();
        bookDetailsResponse.price = Money.convert(book.getPrice(), 2);
        bookDetailsResponse.isbn = book.getIsbn();
        bookDetailsResponse.pages = book.getNumberOfPages();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        bookDetailsResponse.publishAt = dateTimeFormatter.format(book.getPublishAt());
        bookDetailsResponse.authorDetails = AuthorDetails.convert(book.getAuthor());
        return bookDetailsResponse;
    }
}
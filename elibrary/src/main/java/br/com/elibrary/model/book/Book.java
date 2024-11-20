package br.com.elibrary.model.book;

import br.com.elibrary.model.GenericEntity;
import br.com.elibrary.model.author.Author;
import br.com.elibrary.model.category.Category;
import br.com.elibrary.model.common.Money;
import br.com.elibrary.model.exception.DomainValidationException;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tb_book")
@Getter
public class Book extends GenericEntity {
    @Column(name = "title", length = 200, nullable = false)
    private String title;
    @Column(name = "summary", length = 500, nullable = false)
    private String summary;
    @Column(name = "table_of_contents")
    private String tableOfContents;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", nullable = false))
    private Money price;
    @Column(name = "num_of_pages", nullable = false)
    private Short numberOfPages;
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "publish_at")
    private LocalDateTime publishAt;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    /**
     * @deprecated Deprecated in order to avoid accidental usages of this constructor.
     */
    @Deprecated(since = "2024")
    protected Book() {}

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    /**
     * Constructs a Book object or throw an exception if there is a constraint violation
     * @param bookBuilder The builder with the parameters
     * @throws br.com.elibrary.model.exception.DomainValidationException if any constraint is violated
     */
    private Book(BookBuilder bookBuilder) {
        BookValidator bookValidator = new BookValidator();
        if (bookValidator.isValid(bookBuilder)) {
            this.title = bookBuilder.title.trim();
            this.summary = bookBuilder.summary.trim();
            this.tableOfContents = bookBuilder.tableOfContents.trim();
            this.price = new Money(bookBuilder.price);
            this.numberOfPages = bookBuilder.numberOfPages;
            this.isbn = bookBuilder.isbn;
            this.publishAt = LocalDateTime.of(bookBuilder.publishAt, LocalTime.MIDNIGHT);
            this.category = bookBuilder.category;
            this.author = bookBuilder.author;
        } else {
            throw new DomainValidationException("bad.input.for.book", bookValidator.getError());
        }
    }

    public boolean isPublished() {
        return this.publishAt != null && LocalDateTime.now().isAfter(this.publishAt);
    }

    @Getter
    public static class BookBuilder {
        private String title;
        private String summary;
        private String tableOfContents;
        private BigDecimal price;
        private Short numberOfPages;
        private String isbn;
        private LocalDate publishAt;
        private Category category;
        private Author author;

        public BookBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public BookBuilder tableOfContents(String tableOfContents) {
            this.tableOfContents = tableOfContents;
            return this;
        }

        public BookBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public BookBuilder pages(Short numberOfPages) {
            this.numberOfPages = numberOfPages;
            return this;
        }

        public BookBuilder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookBuilder publishAt(LocalDate publishAt) {
            this.publishAt = publishAt;
            return this;
        }

        public BookBuilder author(Author author) {
            this.author = author;
            return this;
        }

        public BookBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
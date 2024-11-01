package br.com.elibrary.model.book;

import br.com.elibrary.model.author.Author;
import br.com.elibrary.model.category.Category;
import br.com.elibrary.model.exception.DomainValidationException;
import br.com.elibrary.model.validation.FieldError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class BookTest {
    @Test
    void shouldNotBuildBookWithInvalidParameters() {
        Book.BookBuilder bookBuilder = Book.builder()
                .title("")
                .isbn(null)
                .tableOfContents("   ")
                .price(BigDecimal.valueOf(10.0))
                .pages((short) 10)
                .publishAt(LocalDate.now().minusDays(10))
                .summary("   ")
                .category(null)
                .author(null);

        DomainValidationException domainValidationException = Assertions.assertThrows(DomainValidationException.class, bookBuilder::build);
        List<String> errors = domainValidationException.getError().getErrors().stream().map(FieldError::code).sorted().toList();

        Assertions.assertEquals(List.of("author.must.exist",
                "category.must.exist",
                "isbn.not.empty",
                "pages.must.not.be.lower.than.100",
                "price.must.not.be.lower.than.20",
                "publish.date.must.not.be.in.the.past",
                "summary.not.empty",
                "table.of.contents.must.not.be.empty",
                "title.must.not.be.null"), errors);
    }

    @Test
    void shouldNotBuildBookWhenFieldsViolateTheirConstraints() {
        Book.BookBuilder bookBuilder = Book.builder()
                .title("The chronicles of Narnia".repeat(100))
                .isbn("12".repeat(26))
                .tableOfContents("""
                        1 - PAGE ONE
                        2 - PAGE TWO
                        3 - PAGE THREE
                        """)
                .price(null)
                .pages(null)
                .publishAt(null)
                .summary("AS342".repeat(101))
                .category(new Category("Horror"))
                .author(new Author("George Orwel", "big.brother@bb.com", "Just"));

        DomainValidationException domainValidationException = Assertions.assertThrows(DomainValidationException.class, bookBuilder::build);
        List<String> errors = domainValidationException.getError().getErrors().stream().map(FieldError::code).sorted().toList();

        Assertions.assertEquals(List.of(
                "isbn.surpasses.allowed.size",
                "pages.must.not.be.lower.than.100",
                "price.not.null",
                "publish.date.must.not.be.null",
                "summary.surpasses.allowed.size",
                "title.surpasses.allowed.size"), errors);
    }

    @Test
    void shouldBuildBookWhenFieldsAreValid() {
        Book book = Book.builder()
                .title("ANIMAL FARM")
                .isbn("223-392039223")
                .tableOfContents("""
                        CHAPTER ONE
                        CHAPTER TWO
                        CHAPTER THREE
                        CHAPTER FOUR
                        """)
                .price(BigDecimal.valueOf(20))
                .pages((short) 250)
                .publishAt(LocalDate.now().plusDays(2))
                .summary("An incredible book")
                .category(new Category("FICTION"))
                .author(new Author("George Orwel", "big.brother@bb.com", "Just"))
                .build();

        Assertions.assertAll(() -> {
            Assertions.assertEquals("ANIMAL FARM", book.getTitle());
            Assertions.assertEquals("223-392039223", book.getIsbn());
            Assertions.assertEquals("""
                        CHAPTER ONE
                        CHAPTER TWO
                        CHAPTER THREE
                        CHAPTER FOUR""", book.getTableOfContents());
            Assertions.assertEquals(new Money(BigDecimal.valueOf(20)), book.getPrice());
            Assertions.assertTrue(book.getPublishAt().isAfter(LocalDateTime.now()));
            Assertions.assertEquals("An incredible book", book.getSummary());
            Assertions.assertNotNull(book.getCategory());
            Assertions.assertNotNull(book.getAuthor());
        });
    }
}
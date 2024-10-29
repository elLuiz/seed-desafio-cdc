package br.com.elibrary.model.book;

import br.com.elibrary.model.exception.DomainValidationException;
import br.com.elibrary.model.validation.FieldError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

class BookTest {
    @Test
    void shouldNotBuildBookWithInvalidParameters() {
        Book.BookBuilder bookBuilder = Book.builder()
                .title("The chronicles of Narnia")
                .isbn(null)
                .tableOfContents("   ")
                .price(BigDecimal.valueOf(10.0))
                .pages(10)
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
                "table.of.contents.must.not.be.empty"), errors);
    }
}
package br.com.elibrary.model.book;

import br.com.elibrary.model.book.Book.BookBuilder;
import br.com.elibrary.model.common.Money;
import br.com.elibrary.model.validation.Error;
import br.com.elibrary.model.validation.FieldError;
import br.com.elibrary.model.validation.Validator;
import br.com.elibrary.util.string.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class BookValidator implements Validator<BookBuilder> {
    private Set<FieldError> fieldErrors;

    @Override
    public Error getError() {
        return new Error(fieldErrors);
    }

    @Override
    public boolean isValid(BookBuilder bookBuilder) {
        validateTitle(bookBuilder);
        validateSummary(bookBuilder);
        validateTableOfContents(bookBuilder);
        validatePrice(bookBuilder.getPrice());
        validateISBN(bookBuilder);
        validatePages(bookBuilder);
        validatePublishAt(bookBuilder);
        validateCategory(bookBuilder);
        validateAuthor(bookBuilder);
        return fieldErrors == null || fieldErrors.isEmpty();
    }

    private void validateTitle(BookBuilder bookBuilder) {
        if (StringUtils.isEmpty(bookBuilder.getTitle())) {
            addError("title", "title.must.not.be.null");
        } else if (StringUtils.isGreaterThan(bookBuilder.getTitle(), 200)) {
            addError("title", "title.surpasses.allowed.size");
        }
    }

    private void validateSummary(BookBuilder bookBuilder) {
        if (StringUtils.isEmpty(bookBuilder.getSummary())) {
            addError("summary", "summary.not.empty");
        } else if (StringUtils.isGreaterThan(bookBuilder.getSummary(), 500)) {
            addError("summary", "summary.surpasses.allowed.size");
        }
    }

    private void validateTableOfContents(BookBuilder bookBuilder) {
        if (StringUtils.isEmpty(bookBuilder.getTableOfContents())) {
            addError("tableOfContents", "table.of.contents.must.not.be.empty");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            addError("price", "price.not.null");
        } else if (new Money(BigDecimal.valueOf(20)).greaterThan(new Money(price))) {
            addError("price", "price.must.not.be.lower.than.20");
        }
    }

    private void validateISBN(BookBuilder bookBuilder) {
        if (StringUtils.isEmpty(bookBuilder.getIsbn())) {
            addError("isbn", "isbn.not.empty");
        } else if (StringUtils.isGreaterThan(bookBuilder.getIsbn(), 50)) {
            addError("isbn", "isbn.surpasses.allowed.size");
        }
    }

    private void validatePages(BookBuilder bookBuilder) {
        if (bookBuilder.getNumberOfPages() == null || bookBuilder.getNumberOfPages() < 100) {
            addError("numberOfPages", "pages.must.not.be.lower.than.100");
        }
    }

    private void validatePublishAt(BookBuilder bookBuilder) {
        if (bookBuilder.getPublishAt() == null) {
            addError("publishAt", "publish.date.must.not.be.null");
        } else if (LocalDate.now().isAfter(bookBuilder.getPublishAt())) {
            addError("publish", "publish.date.must.not.be.in.the.past");
        }
    }

    private void validateCategory(BookBuilder bookBuilder) {
        if (bookBuilder.getCategory() == null) {
            addError("category", "category.must.exist");
        }
    }

    private void validateAuthor(BookBuilder bookBuilder) {
        if (bookBuilder.getAuthor() == null) {
            addError("author", "author.must.exist");
        }
    }

    void addError(String field, String code) {
        if (fieldErrors == null) {
            this.fieldErrors = new HashSet<>();
        }
        this.fieldErrors.add(new FieldError(field, code, code));
    }
}
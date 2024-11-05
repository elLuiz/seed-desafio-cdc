package br.com.elibrary.service.book.command;

import br.com.elibrary.model.book.Book;
import br.com.elibrary.model.validation.NotInThePast;
import br.com.elibrary.model.validation.Unique;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder(toBuilder = true)
public class CreateBookCommand {
    @NotBlank(message = "title.not.empty")
    @Size(max = 200, message = "title.surpasses.allowed.size")
    private String title;
    @NotBlank(message = "summary.not.empty")
    @Size(max = 500, message = "summary.surpasses.allowed.size")
    private String summary;
    @NotBlank(message = "table.of.contents.must.not.be.empty")
    private String tableOfContents;
    @NotNull(message = "price.not.null")
    @Min(value = 20, message = "price.must.not.be.lower.than.20")
    private BigDecimal price;
    @Min(value = 100, message = "pages.must.not.be.lower.than.100")
    private short numberOfPages;
    @NotBlank(message = "isbn.not.empty")
    @Size(max = 50, message = "isbn.surpasses.allowed.size")
    @Unique(field = "isbn", message = "isbn.already.registered", owner = Book.class)
    private String isbn;
    @NotInThePast(message = "publish.date.must.not.be.in.the.past")
    @NotNull(message = "publish.date.must.not.be.null")
    private LocalDate publishAt;
    @NotNull(message = "category.must.not.be.null")
    private Long categoryId;
    @NotNull(message = "author.must.not.be.null")
    private Long authorId;


}
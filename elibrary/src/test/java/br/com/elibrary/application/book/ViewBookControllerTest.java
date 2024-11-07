package br.com.elibrary.application.book;

import br.com.elibrary.application.util.IntegrationTest;
import br.com.elibrary.application.util.RequestSender;
import br.com.elibrary.model.book.Book;
import br.com.elibrary.service.book.BookRepository;
import br.com.elibrary.service.exception.EntityNotFound;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@IntegrationTest
@SqlGroup(value = {
        @Sql(scripts = {"/insert-authors-and-categories.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "/truncate-schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
class ViewBookControllerTest extends RequestSender {
    @Autowired
    BookRepository bookRepository;

    @Test
    void shouldReturnBookDetails() throws Exception {
        Book book = bookRepository.findByISBN("209-394030")
                .orElseThrow(() -> new EntityNotFound("book.not.found", Book.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/{id}", book.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(book.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.summary").value(book.getSummary()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tableOfContents").value(book.getTableOfContents()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(BigDecimal.valueOf(21.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pages").value(150))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publishAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorDetails.authorName").value("GEORGE ORWELL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorDetails.description").value("Just a great author"));
    }

    @Test
    void shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/{id}", 1_000_00L))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.contains("entity.not.found")));
    }
}
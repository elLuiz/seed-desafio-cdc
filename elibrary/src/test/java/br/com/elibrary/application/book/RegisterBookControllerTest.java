package br.com.elibrary.application.book;

import br.com.elibrary.application.util.IntegrationTest;
import br.com.elibrary.application.util.RequestSender;
import br.com.elibrary.service.author.AuthorRepository;
import br.com.elibrary.service.book.command.CreateBookCommand;
import br.com.elibrary.service.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;

@IntegrationTest
@Sql(scripts = {"/insert-authors-and-categories.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class RegisterBookControllerTest extends RequestSender {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void shouldCreateBook() throws Exception {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("DESIGNING DATA INTENSIVE APPLICATIONS");
        createBookCommand.setIsbn("1238293-230239230");
        createBookCommand.setSummary("Wondering how to design a reliable and yet scalable system?");
        createBookCommand.setTableOfContents("TOO MANY CONTENTS");
        createBookCommand.setPublishAt(LocalDate.now().plusDays(2));
        createBookCommand.setNumberOfPages((short) 750);
        createBookCommand.setAuthorId(authorRepository.findByEmail("newman@ms.com").orElseThrow().getId());
        createBookCommand.setPrice(BigDecimal.valueOf(20.0));
        createBookCommand.setCategoryId(categoryRepository.findByName("SOFTWARE ENGINEERING").orElseThrow().getId());

        sendJSON(MockMvcRequestBuilders.post("/api/v1/books")
                .content(objectMapper.writeValueAsBytes(createBookCommand))
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("DESIGNING DATA INTENSIVE APPLICATIONS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publishAt").isNotEmpty());

    }

    @Test
    void shouldNotCreateBookWithSameIsbn() throws Exception {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("DESIGNING DATA INTENSIVE APPLICATIONS");
        createBookCommand.setIsbn("238293-230239230");
        createBookCommand.setSummary("Wondering how to design a reliable and yet scalable system?");
        createBookCommand.setTableOfContents("TOO MANY CONTENTS");
        createBookCommand.setPublishAt(LocalDate.now().plusDays(2));
        createBookCommand.setNumberOfPages((short) 750);
        createBookCommand.setAuthorId(authorRepository.findByEmail("newman@ms.com").orElseThrow().getId());
        createBookCommand.setPrice(BigDecimal.valueOf(20.0));
        createBookCommand.setCategoryId(categoryRepository.findByName("SOFTWARE ENGINEERING").orElseThrow().getId());

        sendJSON(MockMvcRequestBuilders.post("/api/v1/books")
                .content(objectMapper.writeValueAsBytes(createBookCommand))
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        sendJSON(MockMvcRequestBuilders.post("/api/v1/books")
                .content(objectMapper.writeValueAsBytes(createBookCommand))
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].code").value("isbn.already.registered"));
    }
}
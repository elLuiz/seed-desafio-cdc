package br.com.elibrary.application.book;

import br.com.elibrary.application.util.IntegrationTest;
import br.com.elibrary.application.util.RequestSender;
import br.com.elibrary.service.author.AuthorRepository;
import br.com.elibrary.service.book.command.CreateBookCommand;
import br.com.elibrary.service.category.CategoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;

@IntegrationTest
@SqlGroup(value = {
        @Sql(scripts = {"/insert-authors-and-categories.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "/truncate-schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
class RegisterBookControllerTest extends RequestSender {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @ParameterizedTest(name = "Should not create book with unknown author")
    @MethodSource("provideBookWithUnregisteredEntities")
    void shouldReturnNotFoundWhenEntityDoesNotExist(CreateBookCommand createBookCommand, String expectedErrorCode) throws Exception {
        sendJSON(getRequestBuilder(createBookCommand))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(expectedErrorCode));
    }

    static Stream<Arguments> provideBookWithUnregisteredEntities() {
        CreateBookCommand bookWithUnregisteredAuthor = CreateBookCommand.builder()
                .tableOfContents("""
                        CHAPTER 1
                        CHAPTER 2
                        CHAPTER 3
                        CHAPTER 4
                        CHAPTER 5
                        """)
                .numberOfPages((short) 150)
                .summary("A great book discussing the set of trade offs involved while designing and developing distributed applications")
                .categoryId(1L)
                .publishAt(LocalDate.now().plusDays(23))
                .title("DDIA")
                .isbn("209-2939485")
                .price(BigDecimal.valueOf(40.50))
                .authorId(1039L)
            .build();
        CreateBookCommand bookWithUnregisteredCategory = CreateBookCommand.builder()
                .tableOfContents("""
                        CHAPTER 1
                        CHAPTER 2
                        CHAPTER 3
                        CHAPTER 4
                        CHAPTER 5
                        """)
                .numberOfPages((short) 150)
                .summary("A great book discussing the set of trade offs involved while designing and developing distributed applications")
                .categoryId(1L)
                .publishAt(LocalDate.now().plusDays(23))
                .title("DDIA")
                .isbn("209-2939485")
                .price(BigDecimal.valueOf(40.50))
                .authorId(1L)
                .categoryId(10209L)
                .build();
        return Stream.of(
                Arguments.of(bookWithUnregisteredAuthor, "author.not.found"),
                Arguments.of(bookWithUnregisteredCategory, "category.not.found")
        );
    }

    @ParameterizedTest(name = "Book with invalid parameters should return 400 Bad Request")
    @MethodSource("provideBooksWithInvalidArguments")
    void shouldReturnBadRequestForBadFormedInput(CreateBookCommand createBookCommand, List<String> errorCodes) throws Exception {
        sendJSON(getRequestBuilder(createBookCommand))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value(containsInAnyOrder(errorCodes.toArray(new String[0]))));
    }

    static Stream<Arguments> provideBooksWithInvalidArguments() {
        CreateBookCommand bookWithNullParameters = CreateBookCommand.builder()
                .title("")
                .summary(null)
                .tableOfContents("   ")
                .price(null)
                .numberOfPages((short) -10)
                .isbn(null)
                .publishAt(null)
                .categoryId(null)
                .authorId(null)
                .build();
        CreateBookCommand bookContainingInvalidSizes = CreateBookCommand.builder()
                .title("29".repeat(101))
                .summary("50453".repeat(101))
                .tableOfContents("29289")
                .price(BigDecimal.valueOf(19.99))
                .numberOfPages((short) 50)
                .isbn("504".repeat(20))
                .publishAt(LocalDate.now().minusDays(10))
                .categoryId(102L)
                .authorId(9238L)
                .build();

        return Stream.of(
                Arguments.of(bookWithNullParameters, List.of("title.not.empty", "summary.not.empty", "table.of.contents.must.not.be.empty",
                        "price.not.null", "pages.must.not.be.lower.than.100", "isbn.not.empty", "publish.date.must.not.be.null",
                        "category.must.not.be.null", "author.must.not.be.null")),
                Arguments.of(bookContainingInvalidSizes, List.of("title.surpasses.allowed.size", "summary.surpasses.allowed.size",
                        "price.must.not.be.lower.than.20", "pages.must.not.be.lower.than.100", "isbn.surpasses.allowed.size",
                        "publish.date.must.not.be.in.the.past")));
    }

    @DisplayName("Should create book")
    @Test
    void shouldCreateBook() throws Exception {
        CreateBookCommand createBookCommand = CreateBookCommand.builder()
                .title("DESIGNING DATA INTENSIVE APPLICATIONS")
                .isbn("1238293-230239230")
                .summary("Wondering how to design a reliable and yet scalable system?")
                .tableOfContents("TOO MANY CONTENTS")
                .publishAt(LocalDate.now().plusDays(2))
                .numberOfPages((short) 750)
                .authorId(authorRepository.findByEmail("newman@ms.com").orElseThrow().getId())
                .price(BigDecimal.valueOf(20.0))
                .categoryId(categoryRepository.findByName("SOFTWARE ENGINEERING").orElseThrow().getId())
                .build();

        sendJSON(getRequestBuilder(createBookCommand)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("DESIGNING DATA INTENSIVE APPLICATIONS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publishAt").isNotEmpty());

    }

    @DisplayName("Should not create book with the same ISBN")
    @Test
    void shouldNotCreateBookWithSameIsbn() throws Exception {
        CreateBookCommand createBookCommand = CreateBookCommand.builder()
            .title("DESIGNING DATA INTENSIVE APPLICATIONS")
            .isbn("238293-230239230")
            .summary("Wondering how to design a reliable and yet scalable system?")
            .tableOfContents("TOO MANY CONTENTS")
            .publishAt(LocalDate.now().plusDays(2))
            .numberOfPages((short) 750)
            .authorId(authorRepository.findByEmail("newman@ms.com").orElseThrow().getId())
            .price(BigDecimal.valueOf(20.0))
            .categoryId(categoryRepository.findByName("SOFTWARE ENGINEERING").orElseThrow().getId())
            .build();

        sendJSON(getRequestBuilder(createBookCommand)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        sendJSON(getRequestBuilder(createBookCommand)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].code").value("isbn.already.registered"));
    }

    private @NotNull MockHttpServletRequestBuilder getRequestBuilder(CreateBookCommand createBookCommand) throws JsonProcessingException {
        return MockMvcRequestBuilders.post("/api/v1/books")
                .content(objectMapper.writeValueAsBytes(createBookCommand))
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR");
    }
}
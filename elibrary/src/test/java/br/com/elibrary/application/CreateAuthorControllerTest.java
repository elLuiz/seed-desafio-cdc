package br.com.elibrary.application;

import br.com.elibrary.model.request.CreateAuthorRequest;
import br.com.elibrary.utils.RandomStringGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.stream.Stream;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false, print = MockMvcPrint.LOG_DEBUG)
class CreateAuthorControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14");

    @DynamicPropertySource
    static void dbProperties(DynamicPropertyRegistry registry) {
        System.setProperty("SHOW_SQL", "true");
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
    }

    @Test
    void shouldReturnBadRequestWhenAuthorContainsInvalidEmail() throws Exception {
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setName("JUNIT TEST");
        createAuthorRequest.setDescription("A SIMPLE, BUT EXPRESSIVE DESCRIPTION");
        createAuthorRequest.setEmail("et");

        ResultActions actions = send(createAuthorRequest);

        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].code").value("invalid.email"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.occurredAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("email"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNames")
    void shouldReturnErrorWhenNameIsInvalid(String name, String expectedErrorResponse) throws Exception {
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setName(name);
        createAuthorRequest.setDescription("A SIMPLE DESCRIPTION");
        createAuthorRequest.setEmail("email@email.com");

        ResultActions actions = send(createAuthorRequest);

        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].code").value(expectedErrorResponse))
                .andExpect(MockMvcResultMatchers.jsonPath("$.occurredAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("name"));
    }

    static Stream<Arguments> provideInvalidNames() {
        return Stream.of(
                Arguments.of(null, "name.must.not.be.null"),
                Arguments.of("", "name.must.not.be.null"),
                Arguments.of("  ", "name.must.not.be.null"),
                Arguments.of(RandomStringGenerator.fill(256, "T"), "name.with.invalid.size")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDescription")
    void shouldReturnErrorWhenDescriptionIsInvalid(String description, String expectedCode) throws Exception {
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setName("RANDOM NAME");
        createAuthorRequest.setDescription(description);
        createAuthorRequest.setEmail("email@email.com");

        ResultActions actions = send(createAuthorRequest);

        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].code").value(expectedCode))
                .andExpect(MockMvcResultMatchers.jsonPath("$.occurredAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("description"));
    }

    static Stream<Arguments> provideInvalidDescription() {
        return Stream.of(
                Arguments.of(null, "description.must.not.be.null"),
                Arguments.of("", "description.must.not.be.null"),
                Arguments.of("  ", "description.must.not.be.null"),
                Arguments.of(RandomStringGenerator.fill(256, "TES"), "description.with.invalid.size")
        );
    }


    @Test
    void shouldSaveAuthor() throws Exception {
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setName("VALERIA");
        createAuthorRequest.setEmail("test@gc.br");
        createAuthorRequest.setDescription("Hallo! Ich bin Valeria, und Ich komme aus Deutschland. Freut mich!");

        ResultActions actions = send(createAuthorRequest);

        actions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("VALERIA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void shouldNotSaveAuthorWithDuplicateEmail() throws Exception {
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setName("SEB VETTEL");
        createAuthorRequest.setEmail("test1@gc.br");
        createAuthorRequest.setDescription("Hallo! Ich bin Vettel. Ich bin sehr gut.");

        ResultActions actions = send(createAuthorRequest);
        actions.andExpect(MockMvcResultMatchers.status().isCreated());
        actions = send(createAuthorRequest);
        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].code").value("email.already.taken"));

    }

    private ResultActions send(CreateAuthorRequest createAuthorRequest) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authors")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .content(objectMapper.writeValueAsBytes(createAuthorRequest)
                ));
    }
}
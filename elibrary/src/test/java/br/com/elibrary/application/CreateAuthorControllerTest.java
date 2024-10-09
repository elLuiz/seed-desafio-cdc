package br.com.elibrary.application;

import br.com.elibrary.model.request.CreateAuthorRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

    }

    @Test
    void shouldReturnBadRequestWhenAuthorContainsInvalidEmail() throws Exception {
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setName("JUNIT TEST");
        createAuthorRequest.setDescription("A SIMPLE, BUT EXPRESSIVE DESCRIPTION");
        createAuthorRequest.setEmail("et");

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authors")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .content(objectMapper.writeValueAsBytes(createAuthorRequest)
        ));

        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].code").value("invalid.email"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.occurredAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("email"));
    }
}
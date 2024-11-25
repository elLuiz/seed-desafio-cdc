package br.com.elibrary.application.book;

import br.com.elibrary.application.util.IntegrationTest;
import br.com.elibrary.application.util.RequestSender;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@IntegrationTest
@SqlGroup(value = {
        @Sql(scripts = "/insert-authors-and-categories.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "/truncate-schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
class ListBooksControllerTest extends RequestSender {
    @Test
    void shouldListBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[*].title").value(Matchers.contains("1984", "Building Microservices", "Microservices")));
    }

}
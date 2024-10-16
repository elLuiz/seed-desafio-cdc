package br.com.elibrary.application.category;

import br.com.elibrary.application.dto.request.CreateCategoryRequest;
import br.com.elibrary.application.util.RequestSender;
import br.com.elibrary.utils.RandomStringGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CreateCategoryControllerTest extends RequestSender {

    @DisplayName("Should create category when name is valid")
    @ParameterizedTest
    @ValueSource(strings = {"CATEGORY ONE", "AAA89343948"})
    void shouldSaveValidCategory(String name) throws Exception {
        CreateCategoryRequest categoryRequest = new CreateCategoryRequest();
        categoryRequest.setName(name);

        ResultActions actions = this.sendJSON(MockMvcRequestBuilders.post("/api/v1/categories")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .content(objectMapper.writeValueAsBytes(categoryRequest)));

        actions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));
    }

    @DisplayName("Should not create category when name violates constraints")
    @ParameterizedTest
    @MethodSource("provideInvalidNamesWithExpectedErrorCodes")
    void shouldNotCreateCategoryWhenNameIsInvalid(String name, String code) throws Exception {
        CreateCategoryRequest categoryRequest = new CreateCategoryRequest();
        categoryRequest.setName(name);

        ResultActions actions = this.sendJSON(MockMvcRequestBuilders.post("/api/v1/categories")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .content(objectMapper.writeValueAsBytes(categoryRequest)));

        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].code").value(code));
    }
    
    static Stream<Arguments> provideInvalidNamesWithExpectedErrorCodes() {
        return Stream.of(
                Arguments.of("", "category.name.not.empty"),
                Arguments.of("       ", "category.name.not.empty"),
                Arguments.of(null, "category.name.not.empty"),
                Arguments.of(RandomStringGenerator.fill(61, "TE"), "category.name.surpasses.limit")
        );
    }
}
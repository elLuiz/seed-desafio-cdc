package br.com.elibrary.application.country;

import br.com.elibrary.application.dto.request.CreateCountryRequest;
import br.com.elibrary.application.dto.response.CreateStateRequest;
import br.com.elibrary.application.util.IntegrationTest;
import br.com.elibrary.application.util.RequestSender;
import br.com.elibrary.model.country.Country;
import br.com.elibrary.model.country.State;
import br.com.elibrary.service.country.CountryRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;
import java.util.stream.Stream;

@IntegrationTest
@Sql(scripts = {"/insert-countries.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class RegisterCountryControllerTest extends RequestSender {
    @Autowired
    CountryRepository countryRepository;

    @ParameterizedTest(name = "Should not create Country with invalid parameters")
    @MethodSource("provideCountryWithInvalidParameters")
    void shouldNotSaveCountryWithInvalidParameters(String name, String expectedErrorCode) throws Exception {
        sendJSON(MockMvcRequestBuilders.post("/api/v1/countries")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .content(objectMapper.writeValueAsBytes(new CreateCountryRequest(name))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.contains(expectedErrorCode)));
    }

    static Stream<Arguments> provideCountryWithInvalidParameters() {
        return Stream.of(
                Arguments.of("     ", "country.name.must.not.be.empty"),
                Arguments.of(null, "country.name.must.not.be.empty"),
                Arguments.of("102".repeat(100), "country.name.must.not.surpass.limit")
        );
    }

    @ParameterizedTest(name = "Should not create repeated country")
    @MethodSource("provideCountriesWithSameName")
    void shouldNotCreateCountryWithAnAlreadyRegisteredName(String name, int expectedStatusCode) throws Exception {
        sendJSON(MockMvcRequestBuilders.post("/api/v1/countries")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .content(objectMapper.writeValueAsBytes(new CreateCountryRequest(name))))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatusCode));
    }

    static Stream<Arguments> provideCountriesWithSameName() {
        return Stream.of(
                Arguments.of("Brazil", 201),
                Arguments.of(" Brazil ", 400),
                Arguments.of("brazil", 400)
        );
    }

    @DisplayName("Should create country")
    @Test
    void shouldCreateCountry() throws Exception {
        CreateCountryRequest createCountryRequest = new CreateCountryRequest("United States \n\t\r");
        sendJSON(MockMvcRequestBuilders.post("/api/v1/countries")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .content(objectMapper.writeValueAsBytes(createCountryRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("United States"));
    }

    @DisplayName("Should not register state with empty name")
    @Test
    void shouldReturnErrorWhenStatesAreInvalid() throws Exception {
        CreateStateRequest createStateRequest = new CreateStateRequest(Set.of(" MG ", "Minas Gerais", "", "2932".repeat(230)));
        Country country = countryRepository.findByName("DEUTSCHLAND").orElseThrow();
        sendJSON(MockMvcRequestBuilders.post("/api/v1/countries/{id}/states", country.getId())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .content(objectMapper.writeValueAsBytes(createStateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder("state.must.not.be.empty", "state.must.not.surpass.limit")))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Should Register states")
    @Test
    void shouldRegisterStates() throws Exception {
        CreateStateRequest createStateRequest = new CreateStateRequest(Set.of("Baden-Württemberg", "Bavaria", "Berlin", "Brandenburg"));
        Country country = countryRepository.findByName("DEUTSCHLAND").orElseThrow();
        sendJSON(MockMvcRequestBuilders.post("/api/v1/countries/{id}/states", country.getId())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .content(objectMapper.writeValueAsBytes(createStateRequest)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
        country = countryRepository.findById(country.getId()).orElse(null);
        Assertions.assertNotNull(country);
        Assertions.assertEquals(Set.of(new State("Baden-Württemberg"), new State("Bavaria"), new State("Berlin"), new State("Brandenburg")), country.getStates());
    }
}
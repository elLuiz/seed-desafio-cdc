package br.com.elibrary.application.order;


import br.com.elibrary.application.dto.request.RegisterOrderRequest;
import br.com.elibrary.application.util.IntegrationTest;
import br.com.elibrary.application.util.RequestSender;
import br.com.elibrary.service.country.CountryRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
@Sql(scripts = "/insert-countries.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class RegisterOrderControllerTest extends RequestSender {
    @Autowired
    private CountryRepository countryRepository;

    @ParameterizedTest
    @MethodSource("provideOrderWithInvalidArguments")
    void shouldReturnBadRequestForInvalidInput(RegisterOrderRequest request, List<String> expectedErrorCodes) throws Exception {
        sendJSON(MockMvcRequestBuilders.post("/api/v1/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder(expectedErrorCodes.toArray(new String[0]))))
                .andDo(MockMvcResultHandlers.print());
    }

    static Stream<Arguments> provideOrderWithInvalidArguments() {
        RegisterOrderRequest orderWithEmptyFields = new RegisterOrderRequest("", null, "", "", null, null);
        Arguments argumentsForEmptyFields = Arguments.of(orderWithEmptyFields, List.of("email.must.not.be.empty", "name.must.not.be.empty", "document.must.not.be.empty", "document.with.invalid.format", "last.name.must.not.be.empty", "address.must.not.be.null", "cellphone.must.not.be.null"));

        RegisterOrderRequest orderWithFieldViolations = new RegisterOrderRequest("2l3232", "name".repeat(100), "190".repeat(20), "lastName".repeat(60), null, null);
        Arguments argumentsForFieldViolations = Arguments.of(orderWithFieldViolations, List.of("email.must.be.valid", "name.surpasses.max.size", "document.with.invalid.format", "last.name.surpasses.max.size", "address.must.not.be.null", "cellphone.must.not.be.null"));

        return Stream.of(
                argumentsForEmptyFields,
                argumentsForFieldViolations
        );
    }
}
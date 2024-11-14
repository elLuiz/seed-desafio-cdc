package br.com.elibrary.application.order;


import br.com.elibrary.model.GenericEntity;
import br.com.elibrary.service.order.command.CellPhoneCommand;
import br.com.elibrary.service.order.command.OrderAddressCommand;
import br.com.elibrary.service.order.command.RegisterOrderCommand;
import br.com.elibrary.application.util.IntegrationTest;
import br.com.elibrary.application.util.RequestSender;
import br.com.elibrary.service.country.CountryRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
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
    @MethodSource({"provideOrderWithInvalidArguments", "provideOrderWithInvalidAddress", "provideOrderWithInvalidPhoneNumber"})
    void shouldReturnBadRequestForInvalidInput(RegisterOrderCommand request, List<String> expectedErrorCodes) throws Exception {
        sendJSON(MockMvcRequestBuilders.post("/api/v1/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder(expectedErrorCodes.toArray(new String[0]))))
                .andDo(MockMvcResultHandlers.print());
    }

    static Stream<Arguments> provideOrderWithInvalidArguments() {
        RegisterOrderCommand orderWithEmptyFields = new RegisterOrderCommand("", null, "", "", null, null);
        Arguments argumentsForEmptyFields = Arguments.of(orderWithEmptyFields, List.of("email.must.not.be.empty", "name.must.not.be.empty", "document.must.not.be.empty", "document.with.invalid.format", "last.name.must.not.be.empty", "address.must.not.be.null", "cellphone.must.not.be.null"));

        RegisterOrderCommand orderWithFieldViolations = new RegisterOrderCommand("2l3232", "name".repeat(100), "190".repeat(20), "lastName".repeat(60), null, null);
        Arguments argumentsForFieldViolations = Arguments.of(orderWithFieldViolations, List.of("email.must.be.valid", "name.surpasses.max.size", "document.with.invalid.format", "last.name.surpasses.max.size", "address.must.not.be.null", "cellphone.must.not.be.null"));

        return Stream.of(
                argumentsForEmptyFields,
                argumentsForFieldViolations
        );
    }

    static Stream<Arguments> provideOrderWithInvalidAddress() {
        RegisterOrderCommand orderWithEmptyAddressFields = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", new OrderAddressCommand("", "", "", null, null, ""), new CellPhoneCommand(23, "290393842"));
        Arguments argumentsForEmptyFields = Arguments.of(orderWithEmptyAddressFields, List.of("address.must.not.be.empty", "complement.must.not.be.empty", "city.must.not.be.empty", "zip.code.must.not.be.empty", "country.must.not.be.null"));

        RegisterOrderCommand orderWithFieldViolations = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", new OrderAddressCommand("12".repeat(102), "234".repeat(101), "323".repeat(151), "1".repeat(21), 1L, "123".repeat(51)), new CellPhoneCommand(23, "290393842"));
        Arguments argumentsForFieldViolations =  Arguments.of(orderWithFieldViolations, List.of("address.surpasses.max.size", "complement.surpasses.max.size", "city.surpasses.max.size", "zip.code.surpasses.max.size", "state.surpasses.max.size"));

        return Stream.of(
                argumentsForEmptyFields,
                argumentsForFieldViolations
          );
    }

    static Stream<Arguments> provideOrderWithInvalidPhoneNumber() {
        OrderAddressCommand address = new OrderAddressCommand("R. Monaco", "NONE", "Monte Carlo", "29303930", 1L, "MC");
        RegisterOrderCommand orderWithEmptyCellphone = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", address, new CellPhoneCommand(null, ""));
        Arguments argumentForEmptyCellphone = Arguments.of(orderWithEmptyCellphone, List.of("code.must.not.be.null", "phone.number.must.not.be.null", "phone.with.invalid.format"));

        RegisterOrderCommand orderWithInvalidPhone = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", address, new CellPhoneCommand(2322, "43432a2"));
        Arguments argumentForInvalidCellphone = Arguments.of(orderWithInvalidPhone, List.of("code.with.invalid.range", "phone.with.invalid.format"));

        return Stream.of(
                argumentForEmptyCellphone,
                argumentForInvalidCellphone
        );
    }

    @Test
    void shouldNotRegisterOrderWhenStateDoesNotBelongToCountry() throws Exception {
        OrderAddressCommand address = new OrderAddressCommand("R. Monaco", "NONE", "Monte Carlo", "29303930", countryRepository.findByName("deutschland").map(GenericEntity::getId).orElse(null), "Rio de Janeiro");
        CellPhoneCommand cellphone = new CellPhoneCommand(34, "23348575");
        RegisterOrderCommand orderCommand = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", address, cellphone);

        sendJSON(MockMvcRequestBuilders.post("/api/v1/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderCommand)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value("state.does.not.belong.to.country"))
                .andDo(MockMvcResultHandlers.print());
    }

    @ParameterizedTest
    @MethodSource("provideCountryAndState")
    void shouldRegisterOrder(String country, String state) throws Exception {
        OrderAddressCommand address = new OrderAddressCommand("R. Monaco", "NONE", "Monte Carlo", "29303930", countryRepository.findByName(country).map(GenericEntity::getId).orElse(null), state);
        CellPhoneCommand cellphone = new CellPhoneCommand(34, "23348575");
        RegisterOrderCommand orderCommand = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", address, cellphone);

        sendJSON(MockMvcRequestBuilders.post("/api/v1/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderCommand)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    static Stream<Arguments> provideCountryAndState() {
        return Stream.of(
                Arguments.of("Deutschland", "Berlin"),
                Arguments.of("bosnia", null)
        );
    }
}
package br.com.elibrary.application.order;


import br.com.elibrary.application.util.IntegrationTest;
import br.com.elibrary.application.util.RequestSender;
import br.com.elibrary.model.GenericEntity;
import br.com.elibrary.service.book.BookRepository;
import br.com.elibrary.service.country.CountryRepository;
import br.com.elibrary.service.order.command.CellPhoneCommand;
import br.com.elibrary.service.order.command.OrderAddressCommand;
import br.com.elibrary.service.order.command.OrderDetailsCommand;
import br.com.elibrary.service.order.command.OrderItemCommand;
import br.com.elibrary.service.order.command.RegisterOrderCommand;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@IntegrationTest
@Sql(scripts = {"/insert-countries.sql", "/insert-authors-and-categories.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class RegisterOrderControllerTest extends RequestSender {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private BookRepository bookRepository;
    private static OrderDetailsCommand orderDetailsCommand;

    @BeforeEach
    void setUp() {
        orderDetailsCommand = new OrderDetailsCommand(BigDecimal.valueOf(42.0), List.of(new OrderItemCommand(bookRepository.findByISBN("209-394030").map(GenericEntity::getId).orElse(null), 2)));
    }

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
        RegisterOrderCommand orderWithEmptyFields = new RegisterOrderCommand("", null, "", "", null, null, orderDetailsCommand);
        Arguments argumentsForEmptyFields = Arguments.of(orderWithEmptyFields, List.of("email.must.not.be.empty", "name.must.not.be.empty", "document.must.not.be.empty", "document.with.invalid.format", "last.name.must.not.be.empty", "address.must.not.be.null", "cellphone.must.not.be.null", "order.details.must.not.be.null"));

        RegisterOrderCommand orderWithFieldViolations = new RegisterOrderCommand("2l3232", "name".repeat(100), "190".repeat(20), "lastName".repeat(60), null, null, orderDetailsCommand);
        Arguments argumentsForFieldViolations = Arguments.of(orderWithFieldViolations, List.of("email.must.be.valid", "name.surpasses.max.size", "document.with.invalid.format", "last.name.surpasses.max.size", "address.must.not.be.null", "cellphone.must.not.be.null", "order.details.must.not.be.null"));

        return Stream.of(
                argumentsForEmptyFields,
                argumentsForFieldViolations
        );
    }

    static Stream<Arguments> provideOrderWithInvalidAddress() {
        RegisterOrderCommand orderWithEmptyAddressFields = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", new OrderAddressCommand("", "", "", null, null, ""), new CellPhoneCommand(23, "290393842"), orderDetailsCommand);
        Arguments argumentsForEmptyFields = Arguments.of(orderWithEmptyAddressFields, List.of("address.must.not.be.empty", "complement.must.not.be.empty", "city.must.not.be.empty", "zip.code.must.not.be.empty", "country.must.not.be.null"));

        RegisterOrderCommand orderWithFieldViolations = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", new OrderAddressCommand("12".repeat(102), "234".repeat(101), "323".repeat(151), "1".repeat(21), 1L, "123".repeat(51)), new CellPhoneCommand(23, "290393842"), orderDetailsCommand);
        Arguments argumentsForFieldViolations =  Arguments.of(orderWithFieldViolations, List.of("address.surpasses.max.size", "complement.surpasses.max.size", "city.surpasses.max.size", "zip.code.surpasses.max.size", "state.surpasses.max.size"));

        return Stream.of(
                argumentsForEmptyFields,
                argumentsForFieldViolations
          );
    }

    static Stream<Arguments> provideOrderWithInvalidPhoneNumber() {
        OrderAddressCommand address = new OrderAddressCommand("R. Monaco", "NONE", "Monte Carlo", "29303930", 1L, "MC");
        RegisterOrderCommand orderWithEmptyCellphone = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", address, new CellPhoneCommand(null, ""), orderDetailsCommand);
        Arguments argumentForEmptyCellphone = Arguments.of(orderWithEmptyCellphone, List.of("code.must.not.be.null", "phone.number.must.not.be.null", "phone.with.invalid.format"));

        RegisterOrderCommand orderWithInvalidPhone = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", address, new CellPhoneCommand(2322, "43432a2"), orderDetailsCommand);
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
        RegisterOrderCommand orderCommand = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", address, cellphone, orderDetailsCommand);

        sendJSON(MockMvcRequestBuilders.post("/api/v1/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderCommand)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code").value("state.does.not.belong.to.country"))
                .andDo(MockMvcResultHandlers.print());
    }

    @ParameterizedTest(name = "#{index} - Should register Order with country={0} and state={1}")
    @MethodSource("provideCountryAndState")
    void shouldRegisterOrder(String country, String state) throws Exception {
        OrderAddressCommand address = new OrderAddressCommand("R. Monaco", "NONE", "Monte Carlo", "29303930", countryRepository.findByName(country).map(GenericEntity::getId).orElse(null), state);
        CellPhoneCommand cellphone = new CellPhoneCommand(34, "23348575");
        RegisterOrderCommand orderCommand = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", address, cellphone, orderDetailsCommand);

        sendJSON(MockMvcRequestBuilders.post("/api/v1/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderCommand)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addressInfo").value("R. Monaco"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.complement").value("NONE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Monte Carlo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("29303930"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country", Matchers.equalToIgnoringCase(country)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(state))
                .andDo(MockMvcResultHandlers.print());
    }

    static Stream<Arguments> provideCountryAndState() {
        return Stream.of(
                Arguments.of("Deutschland", "Berlin"),
                Arguments.of("bosnia", null)
        );
    }

    @ParameterizedTest(name = "#{index} Should fail when order contains invalid order items")
    @MethodSource("provideOrderWithInvalidItems")
    void shouldReturnBadRequestWhenOrderContainsInvalidItems(BigDecimal total, List<RegisterBookByISBNCommand> bookByISBNCommands, List<String> expectedErrors) throws Exception {
        OrderAddressCommand address = new OrderAddressCommand("R. Monaco", "NONE", "Monte Carlo", "29303930", countryRepository.findByName("Deutschland").map(GenericEntity::getId).orElse(null), "Berlin");
        CellPhoneCommand cellphone = new CellPhoneCommand(34, "23348575");
        OrderDetailsCommand orderDetails = new OrderDetailsCommand(total, getOrderItemCommands(bookByISBNCommands));
        RegisterOrderCommand orderCommand = new RegisterOrderCommand("email@valid.com", "Max", "22194292400", "Verstappen", address, cellphone, orderDetails);
        sendJSON(MockMvcRequestBuilders.post("/api/v1/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(orderCommand)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder(expectedErrors.toArray(new String[0]))))
                .andDo(MockMvcResultHandlers.print());
    }

    private List<OrderItemCommand> getOrderItemCommands(List<RegisterBookByISBNCommand> bookByISBNCommands) {
        if (bookByISBNCommands == null) {
            return null;
        }
        return bookByISBNCommands.stream().map(registerBookByISBNCommand -> {
            if (registerBookByISBNCommand.isbn() != null) {
                Long bookId = bookRepository.findByISBN(registerBookByISBNCommand.isbn()).map(GenericEntity::getId).orElse(-1L);
                return new OrderItemCommand(bookId, registerBookByISBNCommand.quantity());
            }
            return new OrderItemCommand(null, registerBookByISBNCommand.quantity());
        }).toList();
    }

    static Stream<Arguments> provideOrderWithInvalidItems() {
        Arguments orderWithNullTotalAndBookDoesNotExistAndQuantityIsNull = Arguments.of(null, List.of(new RegisterBookByISBNCommand("120-3930930", null)), List.of("book.does.not.exist", "total.must.not.be.null", "quantity.must.not.be.null"));
        Arguments orderWithNonMatchingTotalAndInvalidQuantity = Arguments.of(BigDecimal.valueOf(221.0), List.of(new RegisterBookByISBNCommand("209-394030", 21)), List.of("order.does.not.match.total", "quantity.must.not.be.greater.than.20"));
        Arguments orderWithNegativeTotalAndQuantity = Arguments.of(BigDecimal.valueOf(-221.0), List.of(new RegisterBookByISBNCommand("209-394030", -1)), List.of("total.must.be.positive", "order.does.not.match.total", "quantity.must.be.positive"));
        Arguments orderWithNullBookId = Arguments.of(BigDecimal.valueOf(20.0), List.of(new RegisterBookByISBNCommand(null, 10)), List.of("bookId.must.not.be.null"));
        Arguments orderWithUnpublishedBook = Arguments.of(BigDecimal.valueOf(121.50), List.of(new RegisterBookByISBNCommand("109-394030", 1)), List.of("book.not.available.yet"));
        Arguments orderWithoutItems = Arguments.of(BigDecimal.valueOf(121.50), null, List.of("items.must.not.be.null", "items.must.not.be.empty"));

        return Stream.of(orderWithNullTotalAndBookDoesNotExistAndQuantityIsNull,
                orderWithNonMatchingTotalAndInvalidQuantity,
                orderWithNegativeTotalAndQuantity,
                orderWithNullBookId,
                orderWithUnpublishedBook,
                orderWithoutItems);
    }
}
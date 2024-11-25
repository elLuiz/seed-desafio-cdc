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
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

@IntegrationTest
@Sql(scripts = {"/insert-authors-and-categories.sql", "/insert-countries.sql", "/insert-coupons.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ViewOrderControllerTest extends RequestSender {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CountryRepository countryRepository;

    @Test
    void shouldReturnBadRequestWhenOrderDoesNotExist() throws Exception {
        sendJSON(MockMvcRequestBuilders.get("/api/v1/orders/{id}", 1_000_000))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldViewOrderWithCouponApplied() throws Exception {
        OrderAddressCommand orderAddressCommand = new OrderAddressCommand("R. Junit", "NONE", "CITY A", "39000", countryRepository.findByName("bosnia").map(GenericEntity::getId).orElse(null), null);
        OrderDetailsCommand orderDetailsCommand = new OrderDetailsCommand(BigDecimal.valueOf(163.50), List.of(new OrderItemCommand(getBookId("209-394030"), 2), new OrderItemCommand(getBookId("219-394030"), 1)));
        RegisterOrderCommand registerOrderCommand = new RegisterOrderCommand("junit@junit.com", "JUNIT", "11122233344", "TESTE", orderAddressCommand, new CellPhoneCommand(34, "99981121"), orderDetailsCommand, "JUNIT25");

        String location = sendJSON(MockMvcRequestBuilders.post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerOrderCommand)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        sendJSON(MockMvcRequestBuilders.get("/api/v1/orders/{id}", getOrderId(location))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("JUNIT TESTE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.document").value("111.222.333-44"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("(34) 9998-1121"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PENDING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.coupon.code").value("JUNIT25"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderTotal", Matchers.is(163.50)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderTotalWithDiscount", Matchers.is(122.63)));
    }

    @Test
    void shouldViewOrderById() throws Exception {
        OrderAddressCommand orderAddressCommand = new OrderAddressCommand("R. Junit", "NONE", "CITY A", "39000", countryRepository.findByName("bosnia").map(GenericEntity::getId).orElse(null), null);
        OrderDetailsCommand orderDetailsCommand = new OrderDetailsCommand(BigDecimal.valueOf(142.50), List.of(new OrderItemCommand(getBookId("209-394030"), 1), new OrderItemCommand(getBookId("219-394030"), 1)));
        RegisterOrderCommand registerOrderCommand = new RegisterOrderCommand("junit@junit.com", "JUNIT", "11222333444555", "TESTE", orderAddressCommand, new CellPhoneCommand(34, "99981121"), orderDetailsCommand, null);

        String location = sendJSON(MockMvcRequestBuilders.post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerOrderCommand)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        sendJSON(MockMvcRequestBuilders.get("/api/v1/orders/{id}", getOrderId(location))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("JUNIT TESTE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.document").value("11.222.333/4445-55"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("(34) 9998-1121"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PENDING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.coupon").value(IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderTotal", Matchers.is(142.50)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderTotalWithDiscount").value(IsNull.nullValue()));
    }

    private Long getBookId(String isbn) {
        return bookRepository.findByISBN(isbn)
                .map(GenericEntity::getId)
                .orElse(null);
    }

    Long getOrderId(String location) {
        String[] parts = location.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}
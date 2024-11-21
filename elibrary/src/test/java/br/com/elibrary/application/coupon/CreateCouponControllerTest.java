package br.com.elibrary.application.coupon;

import br.com.elibrary.application.util.IntegrationTest;
import br.com.elibrary.application.util.RequestSender;
import br.com.elibrary.service.coupon.command.CreateCouponCommand;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
class CreateCouponControllerTest extends RequestSender {
    @ParameterizedTest(name = "Should return 400 Bad Request when payload contains invalid data")
    @MethodSource("provideInvalidCoupons")
    void shouldReturnErrorWhenPayloadIsInvalid(CreateCouponCommand createCouponCommand, List<String> expectedCodes) throws Exception {
        sendJSON(MockMvcRequestBuilders.post("/api/v1/coupons")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createCouponCommand)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder(expectedCodes.toArray(new String[0]))));
    }

    static Stream<Arguments> provideInvalidCoupons() {
        CreateCouponCommand couponWithNullFields = new CreateCouponCommand(null, null, null);
        Arguments argumentsForCouponWithNullFields = Arguments.of(couponWithNullFields, List.of("code.must.not.be.empty", "discount.must.not.be.null", "expiry.must.not.be.null"));

        CreateCouponCommand couponCommandWithEmptyFields = new CreateCouponCommand(" ", -10, LocalDateTime.now().minusMinutes(10));
        Arguments argumentsForCouponWithEmptyFields = Arguments.of(couponCommandWithEmptyFields, List.of("code.must.not.violate.pattern", "discount.must.not.be.zero.or.negative", "expiry.must.not.be.in.the.past"));

        CreateCouponCommand couponWithNegativeDiscount = new CreateCouponCommand("NAME-10", 0, LocalDateTime.now().plusDays(10));
        Arguments argumentsForCouponWithNegativedDiscount = Arguments.of(couponWithNegativeDiscount, List.of("code.must.not.violate.pattern", "discount.must.not.be.zero.or.negative"));

        CreateCouponCommand couponWithExceedingDiscount = new CreateCouponCommand("NAME10", 101, LocalDateTime.now().plusDays(10));
        Arguments argumentsForCouponWithExceedingDiscount = Arguments.of(couponWithExceedingDiscount, List.of("discount.must.not.be.greater.than.100"));

        return Stream.of(
            argumentsForCouponWithNullFields,
            argumentsForCouponWithEmptyFields,
            argumentsForCouponWithNegativedDiscount,
            argumentsForCouponWithExceedingDiscount
        );
    }

    @Test
    void shouldCreateCoupon() throws Exception {
        CreateCouponCommand createCouponCommand = new CreateCouponCommand("junit20", 20, LocalDateTime.now().plusDays(1).withHour(23).withMinute(59));
        sendJSON(MockMvcRequestBuilders.post("/api/v1/coupons")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCouponCommand)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("JUNIT20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discount").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiresAt", Matchers.containsString("23:59:59 +0000")));
    }

    @Test
    void shouldNotCreateDuplicatedCoupon() throws Exception {
        CreateCouponCommand createCouponCommand = new CreateCouponCommand("junit30", 20, LocalDateTime.now().plusDays(1).withHour(23).withMinute(59));
        sendJSON(MockMvcRequestBuilders.post("/api/v1/coupons")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCouponCommand)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        sendJSON(MockMvcRequestBuilders.post("/api/v1/coupons")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCouponCommand)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.contains("code.must.be.unique")));
    }
}
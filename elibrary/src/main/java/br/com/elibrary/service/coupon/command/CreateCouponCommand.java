package br.com.elibrary.service.coupon.command;

import br.com.elibrary.model.coupon.Coupon;
import br.com.elibrary.model.validation.Unique;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateCouponCommand(@NotNull(message = "code.must.not.be.empty")
                                  @Unique(field = "code", message = "code.must.be.unique", owner = Coupon.class)
                                  @Pattern(regexp = "[a-zA-Z\\d]{1,50}", message = "code.must.not.violate.pattern") String code,
                                  @NotNull(message = "discount.must.not.be.null")
                                  @Positive(message = "discount.must.not.be.zero.or.negative")
                                  @Max(value = 100, message = "discount.must.not.be.greater.than.100") Integer discountAmount,
                                  @NotNull(message = "expiry.must.not.be.null")
                                  @Future(message = "expiry.must.not.be.in.the.past") LocalDateTime expiresAt) {
    public Coupon toModel() {
        return new Coupon(code(), discountAmount(), expiresAt());
    }
}
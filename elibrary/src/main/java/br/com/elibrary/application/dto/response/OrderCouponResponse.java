package br.com.elibrary.application.dto.response;

import br.com.elibrary.model.coupon.Coupon;

import java.util.Optional;

public record OrderCouponResponse(Long id, String code, Integer discount) {
    public static Optional<OrderCouponResponse> convert(Coupon coupon) {
        if (coupon == null) {
            return Optional.empty();
        }
        return Optional.of(new OrderCouponResponse(coupon.getId(), coupon.getCode(), coupon.getDiscount()));
    }
}
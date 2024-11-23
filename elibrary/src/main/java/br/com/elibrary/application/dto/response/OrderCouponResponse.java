package br.com.elibrary.application.dto.response;

import br.com.elibrary.model.coupon.Coupon;

public record OrderCouponResponse(Long id, String code, Integer discount) {
    public static OrderCouponResponse convert(Coupon coupon) {
        return new OrderCouponResponse(coupon.getId(), coupon.getCode(), coupon.getDiscount());
    }
}
package br.com.elibrary.application.dto.response;

import br.com.elibrary.model.coupon.Coupon;

import java.time.format.DateTimeFormatter;

public record CouponCreatedResponse(Long id, String code, Integer discount, String expiresAt) {
    public static CouponCreatedResponse convert(Coupon coupon) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        return new CouponCreatedResponse(coupon.getId(), coupon.getCode(), coupon.getDiscount(), dateTimeFormatter.format(coupon.getExpiresAt()));
    }
}

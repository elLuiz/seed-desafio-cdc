package br.com.elibrary.application.coupon;

import br.com.elibrary.model.coupon.Coupon;
import jakarta.validation.constraints.Pattern;

import java.util.Optional;

public interface CouponRepository {
    void add(Coupon coupon);

    Optional<Coupon> findByCode(@Pattern(regexp = "[a-zA-Z\\d]{1,50}", message = "invalid.coupon.code") String s);
}

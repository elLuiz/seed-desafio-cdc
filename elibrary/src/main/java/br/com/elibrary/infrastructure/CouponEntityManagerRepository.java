package br.com.elibrary.infrastructure;

import br.com.elibrary.application.coupon.CouponRepository;
import br.com.elibrary.model.coupon.Coupon;
import org.springframework.stereotype.Repository;

@Repository
class CouponEntityManagerRepository extends GenericRepository<Coupon, Long> implements CouponRepository {
    CouponEntityManagerRepository() {
        super(Coupon.class);
    }
}
package br.com.elibrary.application.coupon;

import br.com.elibrary.model.coupon.Coupon;
import br.com.elibrary.service.coupon.command.CreateCouponCommand;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CreateCouponService {
    private final CouponRepository couponRepository;

    public CreateCouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional
    public Coupon save(CreateCouponCommand createCouponCommand) {
        Coupon coupon = createCouponCommand.toModel();
        this.couponRepository.add(coupon);
        return coupon;
    }
}
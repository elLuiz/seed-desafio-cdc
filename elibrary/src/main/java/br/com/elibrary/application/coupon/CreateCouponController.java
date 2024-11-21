package br.com.elibrary.application.coupon;

import br.com.elibrary.application.dto.response.CouponCreatedResponse;
import br.com.elibrary.application.util.HttpHeaderUtil;
import br.com.elibrary.model.coupon.Coupon;
import br.com.elibrary.service.coupon.command.CreateCouponCommand;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
public class CreateCouponController {
    private final CreateCouponService couponService;

    public CreateCouponController(CreateCouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CouponCreatedResponse> create(@RequestBody @Valid CreateCouponCommand createCouponCommand) {
        Coupon coupon = couponService.save(createCouponCommand);
        return ResponseEntity.created(HttpHeaderUtil.getLocationURI("/{id}", coupon.getId()))
                .body(CouponCreatedResponse.convert(coupon));
    }
}
package br.com.elibrary.service.order;

import br.com.elibrary.application.coupon.CouponRepository;
import br.com.elibrary.model.country.Country;
import br.com.elibrary.model.country.State;
import br.com.elibrary.model.coupon.Coupon;
import br.com.elibrary.model.exception.DomainException;
import br.com.elibrary.model.order.Address;
import br.com.elibrary.model.order.Order;
import br.com.elibrary.service.country.CountryRepository;
import br.com.elibrary.service.exception.EntityNotFound;
import br.com.elibrary.service.order.command.RegisterOrderCommand;
import br.com.elibrary.util.string.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterOrderService {
    private final CountryRepository countryRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;

    public RegisterOrderService(CountryRepository countryRepository,
                                OrderItemMapper orderItemMapper,
                                OrderRepository orderRepository,
                                CouponRepository couponRepository) {
        this.countryRepository = countryRepository;
        this.orderItemMapper = orderItemMapper;
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Order register(RegisterOrderCommand registerOrderCommand) {
        Country country = countryRepository.findById(registerOrderCommand.address().country()).orElseThrow(() -> new EntityNotFound("country.not.found", Country.class));
        State state = country.getStateOrElse(registerOrderCommand.address().state(), () -> new DomainException("state.does.not.belong.to.country")).orElse(null);
        Order order = Order.builder()
                .customerFirstName(registerOrderCommand.name())
                .lastName(registerOrderCommand.lastName())
                .customerEmail(registerOrderCommand.email())
                .document(registerOrderCommand.document())
                .cellPhone(registerOrderCommand.cellphone().code(), registerOrderCommand.cellphone().phoneNumber())
                .address(Address.builder()
                        .address(registerOrderCommand.address().address())
                        .complement(registerOrderCommand.address().complement())
                        .city(registerOrderCommand.address().city())
                        .zipCode(registerOrderCommand.address().zipCode())
                        .country(country)
                        .state(state)
                        .build())
                .items(orderItemMapper.convert(registerOrderCommand))
                .build();
        if (StringUtils.isNotEmpty(registerOrderCommand.couponCode())) {
            Coupon coupon = couponRepository.findByCode(registerOrderCommand.couponCode())
                    .orElseThrow(() -> new EntityNotFound("coupon.code.not.found", Coupon.class));
            order.applyCoupon(coupon);
        }
        orderRepository.add(order);
        return order;
    }
}
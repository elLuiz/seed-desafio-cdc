package br.com.elibrary.application.dto.response;

import br.com.elibrary.application.dto.response.order.AddressResponse;
import br.com.elibrary.model.order.Order;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class OrderDetailsResponse {
    private String customerName;
    private String document;
    private String phoneNumber;
    private String status;
    private AddressResponse address;
    private List<OrderItemResponse> items;
    private OrderCouponResponse coupon;
    private BigDecimal orderTotal;
    private BigDecimal orderTotalWithDiscount;

    public static OrderDetailsResponse convert(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order.cannot.be.null");
        }
        OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();
        orderDetailsResponse.customerName = order.getCustomerFullName();
        orderDetailsResponse.document = order.getDocument().format();
        orderDetailsResponse.phoneNumber = order.getCellphone().format();
        orderDetailsResponse.status = order.getOrderStatus().name();
        orderDetailsResponse.address = AddressResponse.toResponse(order);
        orderDetailsResponse.items = getItems(order);
        orderDetailsResponse.coupon = OrderCouponResponse.convert(order.getCoupon()).orElse(null);
        orderDetailsResponse.orderTotal = order.total().round(2);
        orderDetailsResponse.orderTotalWithDiscount = getTotalWithDiscount(order);
        return orderDetailsResponse;
    }

    private static @NotNull List<OrderItemResponse> getItems(Order order) {
        return order.getOrderItems()
                .stream()
                .map(OrderItemResponse::convert)
                .toList();
    }

    private static @Nullable BigDecimal getTotalWithDiscount(Order order) {
        return order.totalWithDiscount()
                .map(money -> money.round(2))
                .orElse(null);
    }
}
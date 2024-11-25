package br.com.elibrary.application.dto.response;

import br.com.elibrary.model.order.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(Long bookId, Integer quantity, BigDecimal price) {
    public static OrderItemResponse convert(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getBookId(), orderItem.getQuantity(), orderItem.getBookPrice().getAmount());
    }
}
package br.com.elibrary.service.order;

import br.com.elibrary.model.order.Order;

import java.util.Optional;

public interface OrderRepository {
    void add(Order order);

    Optional<Order> findById(Long id);
}

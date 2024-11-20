package br.com.elibrary.service.order;

import br.com.elibrary.model.order.Order;

public interface OrderRepository {
    void add(Order order);
}

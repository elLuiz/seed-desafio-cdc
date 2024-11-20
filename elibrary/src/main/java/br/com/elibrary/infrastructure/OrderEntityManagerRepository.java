package br.com.elibrary.infrastructure;

import br.com.elibrary.model.order.Order;
import br.com.elibrary.service.order.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
class OrderEntityManagerRepository extends GenericRepository<Order, Long> implements OrderRepository {
    public OrderEntityManagerRepository() {
        super(Order.class);
    }
}

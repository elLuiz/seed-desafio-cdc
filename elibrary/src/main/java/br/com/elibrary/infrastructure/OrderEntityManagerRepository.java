package br.com.elibrary.infrastructure;

import br.com.elibrary.model.order.Order;
import br.com.elibrary.service.order.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class OrderEntityManagerRepository extends GenericRepository<Order, Long> implements OrderRepository {
    public OrderEntityManagerRepository() {
        super(Order.class);
    }

    @Override
    public Optional<Order> findById(Long id) {
        Order order = entityManager.createQuery("SELECT DISTINCT order FROM Order order " +
                        "JOIN FETCH order.orderItems " +
                        "LEFT JOIN FETCH order.coupon " +
                        "JOIN FETCH order.address.country " +
                        "WHERE order.id = :id", Order.class)
                .setParameter("id", id)
                .getSingleResult();
        return Optional.ofNullable(order);
    }
}
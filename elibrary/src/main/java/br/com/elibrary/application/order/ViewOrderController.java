package br.com.elibrary.application.order;

import br.com.elibrary.application.dto.response.OrderDetailsResponse;
import br.com.elibrary.model.order.Order;
import br.com.elibrary.service.exception.EntityNotFound;
import br.com.elibrary.service.order.OrderRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class ViewOrderController {
    private final OrderRepository orderRepository;

    public ViewOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OrderDetailsResponse> viewOrder(@PathVariable("id") Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFound("order.not.found.or.not.available", Order.class));
        return ResponseEntity.ok(OrderDetailsResponse.convert(order));
    }
}

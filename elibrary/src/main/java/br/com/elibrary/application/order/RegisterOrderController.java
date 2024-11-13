package br.com.elibrary.application.order;

import br.com.elibrary.model.order.Order;
import br.com.elibrary.service.order.RegisterOrderService;
import br.com.elibrary.service.order.command.RegisterOrderCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class RegisterOrderController {
    private final RegisterOrderService registerOrderService;

    public RegisterOrderController(RegisterOrderService registerOrderService) {
        this.registerOrderService = registerOrderService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> create(@RequestBody @Valid RegisterOrderCommand orderRequest) {
        Order order = registerOrderService.register(orderRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
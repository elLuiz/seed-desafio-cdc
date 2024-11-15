package br.com.elibrary.service.order.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderDetailsCommand(@NotNull(message = "total.must.not.be.null") @Positive(message = "total.must.be.positive") BigDecimal total,
    @NotNull(message = "items.must.not.be.null") List<@Valid OrderItemCommand> items){
}
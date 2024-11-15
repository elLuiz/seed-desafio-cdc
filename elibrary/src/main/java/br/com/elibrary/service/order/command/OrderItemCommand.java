package br.com.elibrary.service.order.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemCommand(@NotNull(message = "bookId.must.not.be.null") Long bookId,
                               @NotNull(message = "quantity.must.not.be.null")
                               @Min(value = 1, message = "quantity.must.be.positive")
                               @Max(value = 20, message = "quantity.must.not.be.greater.than.20") Integer quantity) {}
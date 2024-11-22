package br.com.elibrary.service.order.command;

import br.com.elibrary.model.book.Book;
import br.com.elibrary.model.common.Money;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record RegisterOrderCommand(
        @NotBlank(message = "email.must.not.be.empty")
        @Email(message = "email.must.be.valid") String email,
        @NotBlank(message = "name.must.not.be.empty")
        @Size(max = 120, message = "name.surpasses.max.size") String name,
        @NotBlank(message = "document.must.not.be.empty")
        @Pattern(regexp = "\\d{11,14}", message = "document.with.invalid.format") String document,
        @NotBlank(message = "last.name.must.not.be.empty")
        @Size(max = 120, message = "last.name.surpasses.max.size") String lastName,
        @NotNull(message = "address.must.not.be.null") @Valid OrderAddressCommand address,
        @NotNull(message = "cellphone.must.not.be.null") @Valid CellPhoneCommand cellphone,
        @NotNull(message = "order.details.must.not.be.null") @Valid OrderDetailsCommand orderDetails,
        @Pattern(regexp = "[a-zA-Z\\d]{1,50}", message = "invalid.coupon.code") String couponCode) {

    public boolean matchesTotal(List<Book> books) {
        Map<Long, Money> bookIdToPrice = books.stream().collect(Collectors.toMap(Book::getId, Book::getPrice));
        Money total = orderDetails.items().stream()
                .map(bookItem -> {
                    Money bookPrice = bookIdToPrice.getOrDefault(bookItem.bookId(), new Money(BigDecimal.valueOf(-1L)));
                    return bookPrice.multiply(bookItem.quantity());
                })
                .reduce(new Money(BigDecimal.ZERO), Money::add);
        return new Money(orderDetails.total()).equals(total);
    }
}
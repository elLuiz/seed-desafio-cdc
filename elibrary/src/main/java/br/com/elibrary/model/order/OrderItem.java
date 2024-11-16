package br.com.elibrary.model.order;

import br.com.elibrary.model.common.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

import java.util.Objects;

@Embeddable
@Getter
public class OrderItem {
    @Column(name = "fk_book_id", nullable = false)
    private Long bookId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", nullable = false))
    private Money bookPrice;

    private OrderItem() {}

    public OrderItem(Long bookId, Integer quantity, Money bookPrice) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.bookPrice = bookPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem that = (OrderItem) o;
        return Objects.equals(bookId, that.bookId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, quantity);
    }
}
package br.com.elibrary.model.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
public class Money {
    @Column
    private BigDecimal amount;

    private Money() {
    }

    public Money(BigDecimal amount) {
        this.amount = amount.setScale(6, RoundingMode.HALF_UP);
    }

    public boolean greaterThan(Money money) {
        return this.amount.compareTo(money.amount) > 0;
    }

    public Money add(Money money) {
        return new Money(this.amount.add(money.amount));
    }

    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }

    /**
     * Converts a {@link Money} object into a {@link BigDecimal} representation.
     * @param money The money object.
     * @param decimalPlaces The number of places after the conversion.
     * @return A {@link BigDecimal} with the specified decimal places.
     */
    public static BigDecimal round(Money money, int decimalPlaces) {
       return money.amount.setScale(decimalPlaces, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
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

    public Money discount(int percentageAmount) {
        if (percentageAmount < 0 || percentageAmount > 100) {
            throw new IllegalArgumentException("Percentage %d is out of range".formatted(percentageAmount));
        }
        return new Money(this.amount.subtract(this.amount.multiply(BigDecimal.valueOf((double) percentageAmount / 100))));
    }

    /**
     * Converts a {@link Money} object into a {@link BigDecimal} representation.
     * @param decimalPlaces The number of places after the conversion.
     * @return A {@link BigDecimal} with the specified decimal places.
     */
    public BigDecimal round(int decimalPlaces) {
       return this.amount.setScale(decimalPlaces, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmount() {
        return amount;
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
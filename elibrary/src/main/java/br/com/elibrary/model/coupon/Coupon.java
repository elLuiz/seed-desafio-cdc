package br.com.elibrary.model.coupon;

import br.com.elibrary.model.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "tb_coupon")
@Getter
public class Coupon extends GenericEntity {
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "discount", nullable = false)
    private Integer discount;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    Coupon() {}

    public Coupon(String code, Integer discount, LocalDateTime expiresAt) {
        this.code = code.toUpperCase(Locale.US);
        this.discount = discount;
        this.expiresAt = expiresAt.withSecond(59).atOffset(ZoneOffset.UTC);
    }

    public boolean isExpired() {
        return OffsetDateTime.now(ZoneId.of("UTC"))
                .isAfter(this.expiresAt);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(code, coupon.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code);
    }
}
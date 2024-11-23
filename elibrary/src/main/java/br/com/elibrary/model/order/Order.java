package br.com.elibrary.model.order;

import br.com.elibrary.model.GenericEntity;
import br.com.elibrary.model.common.Money;
import br.com.elibrary.model.coupon.Coupon;
import br.com.elibrary.model.exception.DomainException;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "tb_order")
public class Order extends GenericEntity {
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "customer_first_name", nullable = false)
    private String customerFirstName;

    @Column(name = "customer_last_name", nullable = false)
    private String customerLastName;

    @Embedded
    private Document document;

    @Embedded
    private Address address;

    @Embedded
    private Cellphone cellphone;

    @ElementCollection
    @CollectionTable(name = "tb_order_item", joinColumns = {@JoinColumn(name = "fk_order_id", foreignKey = @ForeignKey(name = "fk_order_id"))})
    private Set<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_coupon_id", foreignKey = @ForeignKey(name = "fk_coupon_id"))
    private Coupon coupon;

    Order() {}

    public String getCustomerFullName() {
        return String.format("%s %s", customerFirstName, customerLastName);
    }

    public Address getAddress() {
        return address;
    }

    public Cellphone getCellphone() {
        return cellphone;
    }

    public Document getDocument() {
        return document;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public Optional<Money> totalWithDiscount() {
        if (coupon == null) {
            return Optional.empty();
        }
        Money total = total();
        return Optional.of(total.discount(coupon.getDiscount()));
    }

    public Money total() {
        return this.orderItems.stream()
                .map(OrderItem::getBookPrice)
                .reduce(new Money(BigDecimal.ZERO), Money::add);
    }

    /**
     * Applies a discount coupon to an order, as long as the coupon is not expired nor nonexistent
     * @param coupon The coupon object
     * @throws DomainException if the coupon is expired or null.
     */
    public void applyCoupon(Coupon coupon) {
        if (coupon == null) {
            throw new DomainException("coupon.cannot.be.null");
        }
        if (coupon.isExpired()) {
            throw new DomainException("coupon.has.expired");
        }
        this.coupon = coupon;
    }

    public static class OrderBuilder {
        private final Order order = new Order();

        public OrderBuilder customerFirstName(String customer) {
            order.customerFirstName = customer;
            return this;
        }

        public OrderBuilder lastName(String lastName) {
            order.customerLastName = lastName;
            return this;
        }

        public OrderBuilder customerEmail(String email) {
            order.email = email;
            return this;
        }

        public OrderBuilder document(String document) {
            order.document = Document.create(document);
            return this;
        }

        public OrderBuilder address(Address addressBuilder) {
            order.address = addressBuilder;
            return this;
        }

        public OrderBuilder cellPhone(Integer code, String phoneNumber) {
            order.cellphone = new Cellphone(code, phoneNumber);
            return this;
        }

        public OrderBuilder items(Set<OrderItem> items) {
            order.orderItems = items;
            return this;
        }

        public Order build() {
            return this.order;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return !super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
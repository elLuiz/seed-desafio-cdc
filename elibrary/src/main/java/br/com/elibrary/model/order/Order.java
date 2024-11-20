package br.com.elibrary.model.order;

import br.com.elibrary.model.GenericEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import java.util.Objects;
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

    Order() {}

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

    public static OrderBuilder builder() {
        return new OrderBuilder();
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
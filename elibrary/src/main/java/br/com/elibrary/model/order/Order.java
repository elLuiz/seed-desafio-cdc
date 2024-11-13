package br.com.elibrary.model.order;

import br.com.elibrary.model.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "tb_order")
public class Order extends GenericEntity {
    @Email
    @Column(name = "column", nullable = false)
    private String email;

    @Column(name = "costumer_first_name", nullable = false)
    private String customerFirstName;

    @Column(name = "costumer_last_name", nullable = false)
    private String customerLastName;

    @Embedded
    private Document document;

    @Embedded
    private Address address;

    @Embedded
    private Cellphone cellphone;

    Order() {}

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
            order.document = new Document();
            return this;
        }

        public OrderBuilder address(Address addressBuilder) {
            order.address = addressBuilder;
            return this;
        }

        public OrderBuilder cellPhone(String code, String phoneNumber) {
            order.cellphone = new Cellphone();
            return this;
        }

        public Order build() {
            return this.order;
        }
    }
}
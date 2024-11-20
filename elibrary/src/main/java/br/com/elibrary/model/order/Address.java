package br.com.elibrary.model.order;

import br.com.elibrary.model.country.Country;
import br.com.elibrary.model.country.State;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    @Column(name = "address", nullable = false)
    private String addressInfo;

    @Column(name = "complement", nullable = false)
    private String complement;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_country_id", foreignKey = @ForeignKey(name = "fk_country_id"))
    private Country country;

    @Column(name = "state")
    @AttributeOverride(name = "name", column = @Column(name = "state", nullable = false))
    private State state;

    protected Address() {}

    public static AddressBuilder builder() {
        return new AddressBuilder();
    }

    public static class AddressBuilder {
        private final Address address = new Address();

        public AddressBuilder address(String addressInfo) {
            address.addressInfo = addressInfo;
            return this;
        }

        public AddressBuilder complement(String complement) {
            address.complement = complement;
            return this;
        }

        public AddressBuilder city(String city) {
            address.city = city;
            return this;
        }

        public AddressBuilder zipCode(String zipCode) {
            address.zipCode = zipCode;
            return this;
        }

        public AddressBuilder country(Country country) {
            address.country = country;
            return this;
        }

        public AddressBuilder state(State state) {
            address.state = state;
            return this;
        }

        public Address build() {
            return this.address;
        }
    }
}
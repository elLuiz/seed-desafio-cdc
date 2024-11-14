package br.com.elibrary.model.order;

import br.com.elibrary.model.country.Country;
import br.com.elibrary.model.country.State;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

@Embeddable
public class Address {
    @Column(name = "address")
    private String addressInfo;

    @Column(name = "complement")
    private String complement;

    @Column(name = "city")
    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;

    @Column(name = "state", nullable = false)
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
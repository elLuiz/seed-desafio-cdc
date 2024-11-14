package br.com.elibrary.service.order;

import br.com.elibrary.model.country.Country;
import br.com.elibrary.model.country.State;
import br.com.elibrary.model.exception.DomainException;
import br.com.elibrary.model.order.Address;
import br.com.elibrary.model.order.Order;
import br.com.elibrary.service.country.CountryRepository;
import br.com.elibrary.service.exception.EntityNotFound;
import br.com.elibrary.service.order.command.RegisterOrderCommand;
import org.springframework.stereotype.Service;

@Service
public class RegisterOrderService {
    private final CountryRepository countryRepository;

    public RegisterOrderService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Order register(RegisterOrderCommand registerOrderCommand) {
        Country country = countryRepository.findById(registerOrderCommand.address().country()).orElseThrow(() -> new EntityNotFound("country.not.found", Country.class));
        State state = country.getStateOrElse(registerOrderCommand.address().state(), () -> new DomainException("state.does.not.belong.to.country")).orElse(null);
        return Order.builder()
                .customerFirstName(registerOrderCommand.name())
                .lastName(registerOrderCommand.lastName())
                .customerEmail(registerOrderCommand.email())
                .document(registerOrderCommand.document())
                .cellPhone(registerOrderCommand.cellphone().code(), registerOrderCommand.cellphone().phoneNumber())
                .address(Address.builder()
                                .address(registerOrderCommand.address().address())
                                .complement(registerOrderCommand.address().complement())
                                .city(registerOrderCommand.address().city())
                                .zipCode(registerOrderCommand.address().zipCode())
                                .country(country)
                                .state(state)
                                .build())
                .build();
    }
}
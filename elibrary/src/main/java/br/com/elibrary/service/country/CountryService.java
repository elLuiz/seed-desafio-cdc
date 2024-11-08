package br.com.elibrary.service.country;

import br.com.elibrary.model.country.Country;
import br.com.elibrary.model.country.State;
import br.com.elibrary.service.exception.EntityNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional
    public void save(Country country) {
        this.countryRepository.add(country);
    }

    @Transactional
    public void addStates(Long countryId, List<State> states) {
        Country country = countryRepository.findById(countryId).orElseThrow(() -> new EntityNotFound("country.not.found", Country.class));
        states.forEach(country::addState);
        this.countryRepository.update(country);
    }
}
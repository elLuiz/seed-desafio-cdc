package br.com.elibrary.service.country;

import br.com.elibrary.model.country.Country;

import java.util.Optional;

public interface CountryRepository {
    Optional<Country> findById(Long id);

    void add(Country country);

    Optional<Country> findByName(String name);

    void update(Country country);
}

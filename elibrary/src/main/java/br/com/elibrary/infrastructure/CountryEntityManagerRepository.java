package br.com.elibrary.infrastructure;

import br.com.elibrary.model.country.Country;
import br.com.elibrary.service.country.CountryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class CountryEntityManagerRepository extends GenericRepository<Country, Long> implements CountryRepository {
    public CountryEntityManagerRepository() {
        super(Country.class);
    }

    @Override
    public Optional<Country> findById(Long id) {
        Country country = entityManager.createQuery("SELECT country FROM Country country " +
                "LEFT JOIN FETCH country.states " +
                "WHERE country.id = :countryId", Country.class)
                .setParameter("countryId", id)
                .getSingleResult();
        return Optional.ofNullable(country);
    }

    @Override
    public Optional<Country> findByName(String name) {
        Country country = entityManager.createQuery("SELECT country FROM Country country " +
                        "LEFT JOIN FETCH country.states " +
                        "WHERE UPPER(TRIM(country.name))=UPPER(TRIM(:name))", Country.class)
                .setParameter("name", name)
                .getSingleResult();
        return Optional.ofNullable(country);
    }
}
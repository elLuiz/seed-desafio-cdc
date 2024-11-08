package br.com.elibrary.application.dto.response.country;

import br.com.elibrary.model.country.Country;

public record CountryCreatedResponse(Long id, String name) {
    public static CountryCreatedResponse convert(Country country) {
        return new CountryCreatedResponse(country.getId(), country.getName());
    }
}

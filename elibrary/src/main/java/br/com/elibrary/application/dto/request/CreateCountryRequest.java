package br.com.elibrary.application.dto.request;

import br.com.elibrary.model.country.Country;
import br.com.elibrary.model.validation.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCountryRequest(@NotBlank(message = "country.name.must.not.be.empty")
                                   @Size(max = 120, message = "country.name.must.not.surpass.limit")
                                   @Unique(field = "name", message = "country.name.must.be.unique", owner = Country.class) String name) {
    public Country convert() {
        return new Country(this.name);
    }
}
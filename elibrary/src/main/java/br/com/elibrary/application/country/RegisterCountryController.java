package br.com.elibrary.application.country;

import br.com.elibrary.application.dto.request.CreateCountryRequest;
import br.com.elibrary.application.dto.response.CountryCreatedResponse;
import br.com.elibrary.application.dto.response.CreateStateRequest;
import br.com.elibrary.application.util.HttpHeaderUtil;
import br.com.elibrary.model.country.Country;
import br.com.elibrary.model.country.State;
import br.com.elibrary.service.country.CountryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class RegisterCountryController {
    private final CountryService countryService;

    public RegisterCountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CountryCreatedResponse> save(@RequestBody @Valid CreateCountryRequest createCountryRequest) {
        Country country = createCountryRequest.convert();
        countryService.save(country);
        CountryCreatedResponse response = CountryCreatedResponse.convert(country);
        return ResponseEntity.created(HttpHeaderUtil.getLocationURI("/{id}", country.getId()))
                .body(response);
    }

    @PostMapping(value = "/{id}/states", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerStates(@PathVariable("id") Long countryId, @RequestBody @Valid CreateStateRequest stateRequest) {
        List<State> states = stateRequest.convert();
        countryService.addStates(countryId, states);
    }
}
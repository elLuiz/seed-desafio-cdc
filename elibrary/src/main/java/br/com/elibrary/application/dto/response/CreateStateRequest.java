package br.com.elibrary.application.dto.response;

import br.com.elibrary.model.country.State;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public record CreateStateRequest(@NotNull(message = "states.must.not.be.null") Set<@NotBlank(message = "state.must.not.be.empty") @Size(max = 120, message = "state.must.not.surpass.limit") String> states) {
    public List<State> convert() {
        if (this.states != null) {
            return this.states
                    .stream()
                    .map(State::new)
                    .toList();
        }
        throw new IllegalArgumentException("states.must.not.be.empty");
    }
}
package br.com.elibrary.model.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {
    private final LocalDateTime occurredAt;
    private final Set<FieldError> errors;

    public Error() {
        this.occurredAt = LocalDateTime.now();
        this.errors = new HashSet<>();
    }

    public Error(Set<FieldError> errors) {
        this.occurredAt = LocalDateTime.now();
        this.errors = errors;
    }

    public void addError(String field, String description, String code) {
        this.errors.add(new FieldError(field, description, code));
    }
}
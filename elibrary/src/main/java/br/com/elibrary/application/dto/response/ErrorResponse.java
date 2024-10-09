package br.com.elibrary.application.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ErrorResponse {
    private final LocalDateTime occurredAt;
    private final Set<FieldError> errors;

    public ErrorResponse() {
        this.occurredAt = LocalDateTime.now();
        this.errors = new HashSet<>();
    }

    public void addError(String field, String description, String code) {
        this.errors.add(new FieldError(field, description, code));
    }
}
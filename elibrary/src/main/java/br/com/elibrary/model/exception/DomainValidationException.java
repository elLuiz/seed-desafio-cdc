package br.com.elibrary.model.exception;

import br.com.elibrary.model.validation.Error;
import lombok.Getter;

@Getter
public class DomainValidationException extends RuntimeException {
    private final transient Error error;

    public DomainValidationException(String message, Error error) {
        super(message);
        this.error = error;
    }
}

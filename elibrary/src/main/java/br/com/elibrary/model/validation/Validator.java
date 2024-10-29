package br.com.elibrary.model.validation;

/**
 * Represents the contract to validate any object
 * @param <T> The type of the object
 */
public interface Validator<T> {
    boolean isValid(T object);
    Error getError();
}

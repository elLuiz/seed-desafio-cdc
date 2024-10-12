package br.com.elibrary.model.validation;

public record FieldError(String field, String description, String code) {
}
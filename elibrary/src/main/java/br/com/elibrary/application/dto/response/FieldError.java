package br.com.elibrary.application.dto.response;

public record FieldError(String field, String description, String code) {
}
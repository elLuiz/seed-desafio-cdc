package br.com.elibrary.service.exception;

import br.com.elibrary.model.GenericEntity;

import java.util.Locale;

public class EntityNotFound extends RuntimeException {
    private final String entity;

    public EntityNotFound(String message, Class<? extends GenericEntity> entity) {
        super(message);
        this.entity = entity.getSimpleName().toLowerCase(Locale.ROOT);
    }

    public EntityNotFound(String message, Throwable cause, Class<? extends GenericEntity> entity) {
        super(message, cause);
        this.entity = entity.getSimpleName().toLowerCase(Locale.ROOT);
    }

    public String getEntity() {
        return entity;
    }
}

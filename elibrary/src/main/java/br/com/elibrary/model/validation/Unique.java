package br.com.elibrary.model.validation;

import br.com.elibrary.model.GenericEntity;
import br.com.elibrary.infrastructure.GenericConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = GenericConstraintValidator.class)
public @interface Unique {
    String field();

    String message();

    Class<? extends GenericEntity> owner();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
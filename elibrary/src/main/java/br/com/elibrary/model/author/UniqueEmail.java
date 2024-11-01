package br.com.elibrary.model.author;

import br.com.elibrary.service.author.UniqueEmailValidatorRepository;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UniqueEmailValidatorRepository.class)
public @interface UniqueEmail {
    String message() default "email.already.taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
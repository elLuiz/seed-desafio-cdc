package br.com.elibrary.model.category;

import br.com.elibrary.service.category.UniqueCategoryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UniqueCategoryValidator.class)
public @interface UniqueCategoryName {
    String message() default "category.already.exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
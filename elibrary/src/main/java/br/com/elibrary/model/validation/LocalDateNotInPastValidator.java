package br.com.elibrary.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class LocalDateNotInPastValidator implements ConstraintValidator<NotInThePast, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate == null || LocalDate.now().isBefore(localDate);
    }
}
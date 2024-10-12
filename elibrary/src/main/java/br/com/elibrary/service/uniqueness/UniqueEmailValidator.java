package br.com.elibrary.service.uniqueness;

import br.com.elibrary.model.validation.UniqueEmail;
import br.com.elibrary.service.AuthorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final AuthorRepository authorRepository;

    public UniqueEmailValidator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return authorRepository.isUnique(email);
    }
}
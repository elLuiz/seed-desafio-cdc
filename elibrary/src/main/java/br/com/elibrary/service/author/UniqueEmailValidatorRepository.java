package br.com.elibrary.service.author;

import br.com.elibrary.model.author.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidatorRepository implements ConstraintValidator<UniqueEmail, String> {
    private final AuthorRepository authorRepository;

    public UniqueEmailValidatorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return authorRepository.isUnique(email);
    }
}
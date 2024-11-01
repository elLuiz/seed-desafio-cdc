package br.com.elibrary.infrastructure;

import br.com.elibrary.model.GenericEntity;
import br.com.elibrary.model.validation.Unique;
import br.com.elibrary.util.string.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenericConstraintValidator implements ConstraintValidator<Unique, String> {
    private Class<? extends GenericEntity> entityClass;
    private String fieldName;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.entityClass = constraintAnnotation.owner();
        this.fieldName = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(input)) {
            return true;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<? extends GenericEntity> from = query.from(entityClass);
        Predicate fieldPredicate = criteriaBuilder.equal(criteriaBuilder.upper(from.get(fieldName)), input.trim().toUpperCase());
        query.select(criteriaBuilder.count(from.get("id"))).where(fieldPredicate);
        Long rows = entityManager.createQuery(query).getSingleResult();
        return rows == null || rows == 0L;
    }
}
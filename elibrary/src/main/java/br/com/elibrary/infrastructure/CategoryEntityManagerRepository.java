package br.com.elibrary.infrastructure;

import br.com.elibrary.model.category.Category;
import br.com.elibrary.service.category.CategoryRepository;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
class CategoryEntityManagerRepository extends GenericRepository<Category, Long> implements CategoryRepository {
    CategoryEntityManagerRepository() {
        super(Category.class);
    }

    @Override
    public void add(Category category) {
        entityManager.persist(category);
    }

    @Override
    public boolean containsName(String categoryName) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT count(id)=0 FROM {h-schema}tb_category WHERE UPPER(TRIM(category_name))=UPPER(TRIM(:categoryName))")
                .setParameter("categoryName", categoryName);
        return Boolean.TRUE.equals(nativeQuery.getSingleResult());
    }
}
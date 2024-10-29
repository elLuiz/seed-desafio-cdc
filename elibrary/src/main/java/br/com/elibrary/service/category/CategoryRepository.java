package br.com.elibrary.service.category;

import br.com.elibrary.model.category.Category;

import java.util.Optional;

public interface CategoryRepository {
    void add(Category category);
    boolean containsName(String categoryName);
    Optional<Category> findById(Long id);
}

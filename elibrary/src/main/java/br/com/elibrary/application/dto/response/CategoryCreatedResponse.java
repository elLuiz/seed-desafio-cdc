package br.com.elibrary.application.dto.response;

import br.com.elibrary.model.category.Category;
import lombok.Getter;

@Getter
public class CategoryCreatedResponse {
    private Long id;
    private String name;

    public CategoryCreatedResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryCreatedResponse convert(Category category) {
        return new CategoryCreatedResponse(category.getId(), category.getName());
    }
}
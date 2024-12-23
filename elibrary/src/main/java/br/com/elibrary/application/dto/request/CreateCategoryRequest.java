package br.com.elibrary.application.dto.request;


import br.com.elibrary.model.category.Category;
import br.com.elibrary.model.validation.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateCategoryRequest {
    @NotBlank(message = "category.name.not.empty")
    @Size(max = 120, message = "category.name.surpasses.limit")
    @Unique(field = "name", message = "category.already.exists", owner = Category.class)
    private String name;

    public Category toModel() {
        return new Category(this.name);
    }
}
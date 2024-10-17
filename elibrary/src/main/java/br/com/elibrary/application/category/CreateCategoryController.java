package br.com.elibrary.application.category;

import br.com.elibrary.application.dto.request.CreateCategoryRequest;
import br.com.elibrary.application.dto.response.CategoryCreatedResponse;
import br.com.elibrary.application.util.HttpHeaderUtil;
import br.com.elibrary.model.category.Category;
import br.com.elibrary.service.category.CategoryRepository;
import br.com.elibrary.util.log.StatefulRequestLogger;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/categories")
@RestController
public class CreateCategoryController {
    private final CategoryRepository categoryRepository;

    public CreateCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional
    @StatefulRequestLogger(action = "Create a category")
    public ResponseEntity<CategoryCreatedResponse> create(@RequestBody @Valid CreateCategoryRequest createCategoryRequest) {
        Category category = createCategoryRequest.toModel();
        categoryRepository.add(category);
        return ResponseEntity.created(HttpHeaderUtil.getLocationURI("/{id}", category.getId()))
                .body(CategoryCreatedResponse.convert(category));
    }
}
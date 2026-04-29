package com.thierso.sass_app.mapper;

import com.thierso.sass_app.entities.Category;
import com.thierso.sass_app.requests.CategoryRequest;
import com.thierso.sass_app.responses.CategoryResponse;
import org.springframework.stereotype.Component;

@Component // facilite l'injection lors de l'utilisation
public class CategoryMapper {
    public Category toEntity(final CategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public CategoryResponse toResponse(final Category entity) {
        final int nbProduit = 0;//entity.getProducts() == null ? 0 : entity.getProducts().size();
        return CategoryResponse.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                //.nbProduct(entity.getProducts().size())
                .nbProduct(nbProduit)
                .build();
    }
}

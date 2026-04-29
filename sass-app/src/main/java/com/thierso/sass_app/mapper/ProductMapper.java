package com.thierso.sass_app.mapper;

import com.thierso.sass_app.entities.Category;
import com.thierso.sass_app.entities.Product;
import com.thierso.sass_app.requests.ProductRequest;
import com.thierso.sass_app.responses.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(final ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .alertThreshold(request.getAlertThreshold())
                .reference(request.getReference())
                .category(Category.builder()
                        .id(request.getCategoryId())
                        .build())
                .build();
    }

    public ProductResponse toResponse(final Product entity) {
        return ProductResponse.builder()
                .name(entity.getName())
                .reference(entity.getReference())
                .price(entity.getPrice())
                .alertThreshold(entity.getAlertThreshold())
                .categoryName(entity.getCategory().getName())
                //entity.availableQuantity()
                .build();
    }
}

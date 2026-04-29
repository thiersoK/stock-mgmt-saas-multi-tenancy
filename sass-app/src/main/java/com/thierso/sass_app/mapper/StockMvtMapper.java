package com.thierso.sass_app.mapper;

import com.thierso.sass_app.entities.Product;
import com.thierso.sass_app.entities.StockMvt;
import com.thierso.sass_app.requests.StockMvtRequest;
import com.thierso.sass_app.responses.StockMvtResponse;
import org.springframework.stereotype.Component;

@Component
public class StockMvtMapper {
    public StockMvt toEntity(final StockMvtRequest request) {
        return StockMvt.builder()
                .dateMvt(request.getDateMvt())
                .comment(request.getComment())
                .typeMvt(request.getTypeMvt())
                .quantity(request.getQuantity())
                .product(Product.builder()
                        .id(request.getProductId())
                        .build())
                .build();
    }

    public StockMvtResponse toResponse(final StockMvt entity) {
        return  StockMvtResponse.builder()
                .dateMvt(entity.getDateMvt())
                .comment(entity.getComment())
                .typeMvt(entity.getTypeMvt())
                .quantity(entity.getQuantity())
                .build();
    }
}

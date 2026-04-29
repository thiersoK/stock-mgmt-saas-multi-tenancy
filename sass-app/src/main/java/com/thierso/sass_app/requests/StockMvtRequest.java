package com.thierso.sass_app.requests;

import com.thierso.sass_app.entities.Product;
import com.thierso.sass_app.entities.TypeMvt;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMvtRequest {

    private TypeMvt typeMvt;

    private Integer quantity;

    private LocalDate dateMvt;

    private String comment;

    private String productId;


}

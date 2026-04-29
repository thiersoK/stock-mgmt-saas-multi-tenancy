package com.thierso.sass_app.responses;

import com.thierso.sass_app.entities.TypeMvt;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMvtResponse {

    private TypeMvt typeMvt;

    private Integer quantity;

    private LocalDate dateMvt;

    private String comment;

    private String product_id;

}

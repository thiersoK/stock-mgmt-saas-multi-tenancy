package com.thierso.sass_app.requests;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    private String name;

    private String reference;

    private String description;

    private Integer alertThreshold;

    private BigDecimal price;

    private String categoryId;

}

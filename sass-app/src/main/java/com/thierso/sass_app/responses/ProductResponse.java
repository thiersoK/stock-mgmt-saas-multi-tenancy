package com.thierso.sass_app.responses;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String name;

    private String reference;

    private String description;

    private Integer alertThreshold;

    private BigDecimal price;

    private String categoryName; // contiendra le nom de la catégorie du produit

    private int availableQuantity; // contiendra la quantité restante du produit
}
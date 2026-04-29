package com.thierso.sass_app.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private  String name;
    private String description;
    private int nbProduct; // Nombre de produits
}

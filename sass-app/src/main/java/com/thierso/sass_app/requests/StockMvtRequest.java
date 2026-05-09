package com.thierso.sass_app.requests;

import com.thierso.sass_app.entities.Product;
import com.thierso.sass_app.entities.TypeMvt;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMvtRequest {
    @NotBlank(message = "Type of movement should not be empty")
    private TypeMvt typeMvt;
    @Positive(message = "Quantity should be a positive number")
    private Integer quantity;
    @NotNull(message = "Date of movement should not be empty")
    @PastOrPresent(message = "Date of movement should be in the past or present")
    private LocalDate dateMvt;
    private String comment;
    @NotBlank(message = "Product ID should not be empty")
    private String productId;
}

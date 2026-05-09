package com.thierso.sass_app.exceptions.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Pour n'inclure que valeur non null dans la reponse
public class ErrorResponse {

    private String code;
    private String message;
    private String path;
    //On peut aussi ajouter le Timestamp si l'on souhaite
    private List<ValidationError> validationErrors;

    //Sous classe de validation d'erreurs
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ValidationError {
        private String field;
        private String code;
        private String message;
    }

}

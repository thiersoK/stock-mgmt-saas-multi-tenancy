package com.thierso.sass_app.exceptions;

import com.thierso.sass_app.exceptions.responses.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Avec cette classe, tu vas pouvoir intercepter les exceptions pour renvoyer une réponse propre.
 * Le rôle : Centraliser la capture des erreurs pour éviter les try/catch partout dans tes services.
 * L'avantage : Tu gardes tes contrôleurs "propres" (ils ne s'occupent que du succès) et ta gestion d'erreur est au même endroit pour toute l'équipe.
 *
 * L'annotation @RestControllerAdvice combine deux concepts Spring :
 *
 * @ControllerAdvice : Permet d'écrire du code qui s'applique à tous les contrôleurs de ton application (au lieu d'écrire la gestion d'erreur dans chaque contrôleur un par un).
 * @ResponseBody : Indique que la réponse sera automatiquement convertie en JSON (ce qui est parfait pour ton API).
 */

@Slf4j
@RestControllerAdvice // ceci indique que ce bean sera utilisé pour gérer les exceptions
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResponse> handleException(
            final BusinessException ex,
            final HttpServletRequest request) {
        log.error("Entity not found", ex);

        //Construction de l'objet de type ErroResponse
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        final HttpStatus status = getHttpStatus(ex);

        return ResponseEntity.status(status)
                .body(errorResponse);
    }



    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(
            final EntityNotFoundException ex,
            final HttpServletRequest request) {
        log.error("Entity not found", ex);

        //Construction de l'objet de type ErroResponse
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("NOT_FOND")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(
            final MethodArgumentNotValidException ex,
            final HttpServletRequest request) {
        log.error("Entity not found", ex);

        final List<ErrorResponse.ValidationError> errors = new ArrayList<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    final String fieldName = ((FieldError) error).getField();
                    final String errorCode = error.getDefaultMessage();
                    final String defaultMessage = error.getDefaultMessage(); //todo add translation later

                    errors.add(ErrorResponse.ValidationError.builder()
                                    .field(fieldName)
                                    .code(errorCode)
                                    .message(defaultMessage)
                                    .build());
                });

        //Construction de l'objet de type ErroResponse
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .validationErrors(errors)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    private HttpStatus getHttpStatus(final BusinessException ex) {

        if (ex instanceof DuplicateRessourceException) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.BAD_REQUEST;
    }
}

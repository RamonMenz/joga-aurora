package br.feevale.joga_aurora.config;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse (
        LocalDateTime timestamp,
        int status,
        String error,
        String message
    ) {}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(final Exception ex) {
        final var status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .body(buildErrorResponse(status, status.getReasonPhrase(), ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(final DataIntegrityViolationException ex) {
        final var message = "Violação de integridade dos dados. Verifique se já existe um registro com os mesmos valores.";
        final var status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(buildErrorResponse(status, message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(final ConstraintViolationException ex) {
        final var message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .findFirst()
                .orElse("Erro de validação.");
        final var status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(buildErrorResponse(status, message));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(final ResponseStatusException ex) {
        final var status = ex.getStatusCode().value();

        return ResponseEntity
                .status(status)
                .body(buildErrorResponse(HttpStatus.valueOf(status), ex.getReason(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        final var status = HttpStatus.BAD_REQUEST;
        final var message = extractError(ex);

        return ResponseEntity
                .status(status)
                .body(buildErrorResponse(status, message));
    }

    private ErrorResponse buildErrorResponse(final HttpStatus status, final String message) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message);
    }

    private ErrorResponse buildErrorResponse(final HttpStatus status, final String error, final String message) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), error, message);
    }

    private String extractError(final MethodArgumentNotValidException ex) {
        final var error = ex.getBindingResult().getAllErrors().stream().findFirst();

        if (error.isEmpty())
            return "erro de validação";

        final var fieldError = (FieldError) error.get();

        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }

}
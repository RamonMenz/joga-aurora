package br.feevale.joga_aurora.config.interceptor;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String VALIDATION_ERROR_MESSAGE = "Erro de validação";

    public record ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message
    ) {
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(final Exception ex) {
        final var status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .body(buildAndLogErrorResponse(status, status.getReasonPhrase(), ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        final var status = HttpStatus.BAD_REQUEST;
        String mensagem = "Erro ao salvar registro. Já existe um item com os mesmos dados.";

        if (Objects.nonNull(ex.getMessage()) && ex.getMessage().contains("uk_classroom")) {
            mensagem = "Já existe uma turma com este nome.";
        }

        return ResponseEntity
                .status(status)
                .body(buildAndLogErrorResponse(status, null, mensagem));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(final ConstraintViolationException ex) {
        final var message = extractError(ex);
        final var status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(buildAndLogErrorResponse(status, null, message));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(final ResponseStatusException ex) {
        final var status = ex.getStatusCode().value();

        return ResponseEntity
                .status(status)
                .body(buildAndLogErrorResponse(HttpStatus.valueOf(status), ex.getReason(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        final var status = HttpStatus.BAD_REQUEST;
        final var message = extractError(ex);

        return ResponseEntity
                .status(status)
                .body(buildAndLogErrorResponse(status, null, message));
    }

    private ErrorResponse buildAndLogErrorResponse(final HttpStatus status, final String error, final String message) {
        ErrorResponse errorResponse;
        if (Objects.isNull(error))
            errorResponse = new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message);
        else
            errorResponse = new ErrorResponse(LocalDateTime.now(), status.value(), error, message);

        log.error("timestamp={} status={} error={} message={}", errorResponse.timestamp, errorResponse.status, errorResponse.error, errorResponse.message);
        return errorResponse;
    }

    private static String extractError(final ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .findFirst()
                .orElse(VALIDATION_ERROR_MESSAGE);
    }

    private static String extractError(final MethodArgumentNotValidException ex) {
        final var error = ex.getBindingResult().getAllErrors().stream().findFirst();
        if (error.isEmpty())
            return VALIDATION_ERROR_MESSAGE;

        final var fieldError = (FieldError) error.get();
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }

}
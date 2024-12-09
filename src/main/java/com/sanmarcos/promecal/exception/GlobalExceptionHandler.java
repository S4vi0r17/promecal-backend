package com.sanmarcos.promecal.exception;

import com.sanmarcos.promecal.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNoEncontradoException(
            UsuarioNoEncontradoException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ContraseniaIncorrectaException.class)
    public ResponseEntity<ErrorResponse> handleContraseniaIncorrectaException(
            ContraseniaIncorrectaException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }
    @ExceptionHandler(ClienteYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleClienteYaExisteException(
            ClienteYaExisteException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler(DniDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleDniDuplicadoException(
            DniDuplicadoException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleClienteNoEncontradoException(
            ClienteNoEncontradoException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }
}

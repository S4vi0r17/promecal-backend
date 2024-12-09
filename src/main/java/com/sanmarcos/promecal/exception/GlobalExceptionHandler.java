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

    @ExceptionHandler({
            UsuarioNoEncontradoException.class,
            OrdenTrabajoNoEncontradaException.class,
            ClienteNoEncontradoException.class,
            NoDataFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler(HistorialNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleHistorialNoEncontradoException(
            HistorialNoEncontradoException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleClienteNoEncontradoException(
            ClienteNoEncontradoException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DocumentoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleDocumentoNoEncontradoException(
            DocumentoNoEncontradoException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + ex.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }
    @ExceptionHandler({
            ContraseniaIncorrectaException.class,
            FechaInvalidaException.class,
            RangoFechaInvalidoException.class,
            ArchivoVacioException.class,
            TipoArchivoInvalidoException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }
    @ExceptionHandler(OrdenTrabajoNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleOrdenTrabajoNoEncontradaException(
            OrdenTrabajoNoEncontradaException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DocumentoEliminacionException.class)
    public ResponseEntity<ErrorResponse> handleDocumentoEliminacionException(
            DocumentoEliminacionException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    @ExceptionHandler({
            ClienteYaExisteException.class,
            DniDuplicadoException.class,
            NumeroSerieDuplicadoException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
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


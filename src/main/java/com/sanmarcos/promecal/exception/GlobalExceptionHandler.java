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
            ProformaServicioNotFoundException.class,
            OrdenTrabajoNoEncontradaException.class,
            ClienteNoEncontradoException.class,
            HistorialNoEncontradoException.class,
            DocumentoNoEncontradoException.class,
            UsuarioNoEncontradoException.class,
            NoDataFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
            UsuariosNoEncontradosException.class,
            UsuarioInvalidException.class,
            UsuarioNoEncontradoException.class,
            DatosInvalidosException.class
    })
    public ResponseEntity<ErrorResponse> handleUsuarioException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);  // O cualquier código HTTP que consideres adecuado
    }
    @ExceptionHandler({
            ProformaServicioException.class,
            InvalidPriceException.class,
            ArchivoVacioException.class,
            TipoArchivoInvalidoException.class,
            ContraseniaIncorrectaException.class,
            FechaInvalidaException.class,
            RangoFechaInvalidoException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({
            PagoYaRegistradoException.class,
            ClienteYaExisteException.class,
            DniDuplicadoException.class,
            NumeroSerieDuplicadoException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({DocumentoEliminacionException.class,InformeDiagnosticoException.class})
    public ResponseEntity<ErrorResponse> handleDocumentoEliminacionException(DocumentoEliminacionException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
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

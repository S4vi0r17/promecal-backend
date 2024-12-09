package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.exception.*;
import com.sanmarcos.promecal.model.dto.*;
import com.sanmarcos.promecal.service.OrdenTrabajoService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/ordentrabajo")
public class OrdenTrabajoController {

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;
    // Endpoint para obtener la lista de órdenes de trabajo con múltiples filtros opcionales
    @GetMapping
    public ResponseEntity<?> obtenerOrdenesTrabajo(
            @RequestParam(required = false) LocalDateTime fechaInicio,
            @RequestParam(required = false) LocalDateTime fechaFin,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String codigo) {
        try {
            List<OrdenTrabajoListaDTO> ordenes = ordenTrabajoService
                    .obtenerOrdenesTrabajoConFiltros(fechaInicio, fechaFin, dni, modelo, codigo);
            return ResponseEntity.ok(ordenes);
        } catch (ClienteNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (RangoFechaInvalidoException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            // Manejo genérico para errores no previstos
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado.");
        }
    }
    @GetMapping("/codigos")
    public ResponseEntity<?> obtenerCodigos() {
        try {
            List<String> codigos = ordenTrabajoService.obtenerCodigos();
            return ResponseEntity.ok(codigos);
        } catch (NoDataFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los códigos.");
        }
    }

    //Endpoint para guardar una orden de trabajo
    @PostMapping
    public ResponseEntity<String> insertarOrdenTrabajo(@RequestPart("orden") @Valid OrdenTrabajoDTO ordenTrabajoDTO,
                                                       @RequestPart(value = "file") MultipartFile file) {
        File tempFile = null;
        try {
            if (file == null || file.isEmpty()) {
                throw new ArchivoVacioException("Debe proporcionar un archivo válido.");
            }
            // Validación: si el tipo de archivo no es PDF
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                throw new TipoArchivoInvalidoException("El archivo debe ser de tipo PDF.");
            }
            ordenTrabajoService.validarOrdenTrabajo(ordenTrabajoDTO, file);
            tempFile = File.createTempFile("documento_remision_", ".pdf");
            file.transferTo(tempFile);
            ordenTrabajoService.insertarOrdenTrabajo(ordenTrabajoDTO, tempFile);
            return ResponseEntity.status(HttpStatus.CREATED).body("Orden de trabajo creada exitosamente.");
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado. Por favor, intente más tarde.");
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
    // Eliminar una orden de trabajo
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarOrdenTrabajo(@PathVariable Long id) {
        try {
            ordenTrabajoService.eliminarOrdenTrabajo(id);
            return ResponseEntity.noContent().build();
        } catch (OrdenTrabajoNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DocumentoEliminacionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al eliminar la orden de trabajo: " + e.getMessage());
        }
    }
    // Endpoint para obtener los detalles de una orden de trabajo por id
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetallesOrdenTrabajo(@PathVariable Long id) {
        try {
            OrdenTrabajoVistaDTO ordenTrabajoVistaDTO = ordenTrabajoService.obtenerOrdenTrabajoPorId(id);
            return new ResponseEntity<>(ordenTrabajoVistaDTO, HttpStatus.OK);
        } catch (OrdenTrabajoNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al obtener los detalles de la orden de trabajo: " + e.getMessage());
        }
    }
    //Endpoint para obtener el historial de modificaciones de un orden de trabajo
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<OrdenTrabajoHistorialDTO>> obtenerHistorialDeOrden(@PathVariable Long id) {
        if (id <= 0) {
            // Validación: el ID debe ser mayor que cero
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        try {
            List<OrdenTrabajoHistorialDTO> historial = ordenTrabajoService.obtenerHistorialDeOrden(id);
            if (historial.isEmpty()) {
                // Si no se encuentra historial, lanzar una excepción o devolver un error adecuado
                throw new HistorialNoEncontradoException("No se encontró historial para la Orden de Trabajo con ID " + id);
            }
            return ResponseEntity.ok(historial);
        } catch (HistorialNoEncontradoException e) {
            // Excepción personalizada si no se encuentra historial
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            // Para cualquier otro error general
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // Endpoint para actualizar la orden de trabajo
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarOrdenTrabajo(@PathVariable Long id,
                                                         @RequestPart("orden") OrdenTrabajoDTO ordenTrabajoDTO,
                                                         @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // Si no se ha subido ningún archivo, se puede proceder con la actualización sin procesarlo
            if (file != null && !file.isEmpty()) {
                // Crear archivo temporal si se ha subido un archivo
                File tempFile = File.createTempFile("documento_remision_", ".pdf");
                file.transferTo(tempFile);
                // Llamar al servicio para actualizar la orden de trabajo, pasando el archivo
                ordenTrabajoService.actualizarOrdenTrabajo(id, ordenTrabajoDTO, tempFile);
            } else {
                // Si no hay archivo, simplemente actualizar la orden de trabajo sin cambios en el archivo
                ordenTrabajoService.actualizarOrdenTrabajo(id, ordenTrabajoDTO, null);  // null indica que no hay archivo
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (OrdenTrabajoNoEncontradaException e) {
            // Excepción personalizada: Orden de trabajo no encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orden de trabajo no encontrada: " + e.getMessage());
        } catch (ClienteNoEncontradoException e) {
            // Excepción personalizada: Cliente no encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado: " + e.getMessage());
        } catch (DocumentoEliminacionException e) {
            // Excepción personalizada: Error en la eliminación del documento
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la eliminación del documento: " + e.getMessage());
        } catch (Exception e) {
            // Para cualquier otro error general
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la orden de trabajo: " + e.getMessage());
        }
    }
}

package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.model.dto.*;
import com.sanmarcos.promecal.service.OrdenTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ordentrabajo")
public class OrdenTrabajoController {

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;
    // Endpoint para obtener la lista de órdenes de trabajo con múltiples filtros opcionales
    @GetMapping
    public List<OrdenTrabajoListaDTO> obtenerOrdenesTrabajo(
            @RequestParam(required = false) LocalDateTime fechaInicio,
            @RequestParam(required = false) LocalDateTime fechaFin,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String codigo) {

        return ordenTrabajoService.obtenerOrdenesTrabajoConFiltros(fechaInicio, fechaFin, dni, modelo, codigo);
    }
    @GetMapping("/codigos")
    public List<String> obtenerCodigos(){
        return ordenTrabajoService.obtenerCodigos();
    }

    //Endpoint para guardar una orden de trabajo
    @PostMapping
    public ResponseEntity<String> insertarOrdenTrabajo(@RequestPart("orden") OrdenTrabajoDTO ordenTrabajoDTO, @RequestPart(value="file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error, no hay archivo");
            }
            File tempFile = File.createTempFile("documento_remision_", ".pdf");
            file.transferTo(tempFile);
            ordenTrabajoService.insertarOrdenTrabajo(ordenTrabajoDTO,tempFile);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar orden de trabajo" + e.getMessage());
        }
    }
    // Eliminar una orden de trabajo
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarOrdenTrabajo(@PathVariable Long id) {
        ordenTrabajoService.eliminarOrdenTrabajo(id);
        return ResponseEntity.noContent().build();
    }
    //Endpoint para obtener los detalles de una orden de trabajo por id
    @GetMapping("/{id}")
    public ResponseEntity<OrdenTrabajoVistaDTO> obtenerDetallesOrdenTrabajo(@PathVariable Long id) {
        try {
            OrdenTrabajoVistaDTO ordenTrabajoVistaDTO=ordenTrabajoService.obtenerOrdenTrabajoPorId(id);
            return new ResponseEntity<>(ordenTrabajoVistaDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    //Endpoint para obtener el historial de modificaciones de un orden de trabajo
    @GetMapping("/{id}/historial")
    public List<OrdenTrabajoHistorialDTO> obtenerHistorialDeOrden(@PathVariable Long id) {
        return ordenTrabajoService.obtenerHistorialDeOrden(id);
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la orden de trabajo: " + e.getMessage());
        }
    }


}

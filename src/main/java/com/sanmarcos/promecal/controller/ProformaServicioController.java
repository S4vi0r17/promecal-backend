package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.exception.*;
import com.sanmarcos.promecal.model.dto.*;
import com.sanmarcos.promecal.service.ProformaServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/proformaservicio")
public class ProformaServicioController {
    @Autowired
    ProformaServicioService proformaServicioService;
    //Endpoint para obtener la lista de proforma de servicios
    @GetMapping
    public ResponseEntity<List<ProformaServicioListaDTO>> obtenerTodosLasProformas() {
        try {
            // Llamar al servicio para obtener las proformas
            List<ProformaServicioListaDTO> proformas = proformaServicioService.obtenerTodosLasProformas();

            // Retornar la lista de proformas como respuesta HTTP 200
            return new ResponseEntity<>(proformas, HttpStatus.OK);
        } catch (ProformaServicioNotFoundException e) {
            // Si no se encontraron proformas, retornar un error HTTP 404 con el mensaje de excepción
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // En caso de un error inesperado, retornar un error genérico HTTP 500
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Endpoint para guardar un cliente
    @PostMapping
    public ResponseEntity<String> insertarProformaServicio(@RequestBody @Valid ProformaServicioDTO proformaServicioDTO) {
        try {
            // Llamar al servicio para insertar la proforma
            proformaServicioService.insertarProformaServicio(proformaServicioDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ProformaServicioException e) {
            // Si ocurre una excepción personalizada de proforma
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (OrdenTrabajoNoEncontradaException e) {
            // Si no se encuentra la orden de trabajo
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            // En caso de otro error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar Proforma de Servicio: " + e.getMessage());
        }
    }

    //Endpoint para obtener los detalles de una proforma de servicio por id
    @GetMapping("/{id}")
    public ResponseEntity<ProformaServicioDTO> obtenerDetallesProformaServicio(@PathVariable Long id) {
        try {
            // Llamamos al servicio para obtener los detalles de la proforma de servicio por id
            ProformaServicioDTO proformaServicioDTO = proformaServicioService.obtenerProformaServicioPorId(id);

            // Si se encuentra la proforma, devolvemos la respuesta con el status 200 OK
            return new ResponseEntity<>(proformaServicioDTO, HttpStatus.OK);
        } catch (ProformaServicioNotFoundException e) {
            // Si ocurre una excepción personalizada (proforma no encontrada), devolvemos el status 404 Not Found con el mensaje de la excepción
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para actualizar una proforma de servicio
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarProformaServicio(@PathVariable Long id, @RequestBody @Valid ProformaServicioDTO proformaServicioDTO) {
        try {
            // Validación: Si el ID es nulo o no corresponde a una proforma existente
            proformaServicioService.actualizarProformaServicio(id, proformaServicioDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProformaServicioNotFoundException e) {
            // Excepción personalizada si no se encuentra la proforma
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proforma de Servicio no encontrada: " + e.getMessage());
        } catch (OrdenTrabajoNoEncontradaException e) {
            // Excepción personalizada si no se encuentra la orden de trabajo
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orden de trabajo no encontrada: " + e.getMessage());
        } catch (InvalidPriceException e) {
            // Excepción personalizada si el precio del servicio no es válido
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Precio del servicio no válido: " + e.getMessage());
        } catch (Exception e) {
            // Excepción genérica para otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la proforma de servicio: " + e.getMessage());
        }
    }
    // Eliminar una proforma de servicio
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProformaServicio(@PathVariable Long id) {
        try {
            proformaServicioService.eliminarProformaServicio(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content si la eliminación es exitosa
        } catch (ProformaServicioNotFoundException e) {
            // Excepción personalizada para cuando no se encuentra la proforma de servicio
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ErrorEliminarProformaServicioException e) {
            // Excepción personalizada para otros errores al eliminar
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            // Excepción genérica
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }

    //Buscar proformas de Servicio por Cliente(dni)
    @GetMapping("/cliente/{dni}")
    public ResponseEntity<List<ProformaServicioListaDTO>> obtenerProformaServicioPorCliente(@PathVariable String dni) {
        try {
            List<ProformaServicioListaDTO> proformas = proformaServicioService.obtenerProformaServicioPorCliente(dni);
            return ResponseEntity.ok(proformas);  // Retorna 200 OK con la lista de proformas
        } catch (ClienteNoEncontradoException e) {
            // Si el cliente no se encuentra, devolver 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ProformaServicioNotFoundException e) {
            // Si no se encuentran proformas para el cliente, devolver 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            // Si el DNI está vacío o nulo, devolver 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Para cualquier otro error, devolver 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/{id}/pago")
    public ResponseEntity<String> registrarPago(@PathVariable Long id, @RequestPart(value = "file") MultipartFile file) {
        try {
            // Validación de archivo vacío
            if (file.isEmpty()) {
                throw new ArchivoVacioException("No se ha recibido un archivo de pago");
            }

            // Validación de tipo de archivo (solo PDF permitido)
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                throw new TipoArchivoInvalidoException("El archivo debe ser de tipo PDF");
            }

            // Crear archivo temporal para almacenar el archivo recibido
            File tempFile = File.createTempFile("boleta_", ".pdf");
            file.transferTo(tempFile);

            // Registrar el pago en el servicio
            proformaServicioService.registrarPago(id, tempFile);

            return ResponseEntity.status(HttpStatus.OK).body("Pago registrado con éxito");

        } catch (ArchivoVacioException | TipoArchivoInvalidoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ProformaServicioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PagoYaRegistradoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());  // Código 409 para conflicto
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar pago: " + e.getMessage());
        }
    }

}

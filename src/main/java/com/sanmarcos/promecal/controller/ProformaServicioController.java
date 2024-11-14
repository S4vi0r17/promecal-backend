package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.model.dto.*;
import com.sanmarcos.promecal.model.entity.Cliente;
import com.sanmarcos.promecal.repository.ProformaServicioRepository;
import com.sanmarcos.promecal.service.ProformaServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/proformaservicio")
public class ProformaServicioController {
    @Autowired
    ProformaServicioService proformaServicioService;
    //Endpoint para obtener la lista de proforma de servicios
    @GetMapping
    public List<ProformaServicioListaDTO> obtenerTodosLosClientes() {
        return proformaServicioService.obtenerTodosLasProformas();
    }
    //Endpoint para guardar un cliente
    @PostMapping
    public ResponseEntity<String> insertarProformaServicio(@RequestBody ProformaServicioDTO proformaServicioDTO) {
        try {
            proformaServicioService.insertarProformaServicio(proformaServicioDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar Proforma de Servicio" + e.getMessage());
        }
    }

    //Endpoint para obtener los detalles de una proforma de servicio por id
    @GetMapping("/{id}")
    public ResponseEntity<ProformaServicioDTO> obtenerDetallesProformaServicio(@PathVariable Long id) {
        try {
            ProformaServicioDTO proformaServicioDTO = proformaServicioService.obtenerProformaServicioPorId(id);
            return new ResponseEntity<>(proformaServicioDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para actualizar una proforma de servicio
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarProformaServicio(@PathVariable Long id, @RequestBody ProformaServicioDTO proformaServicioDTO) {
        try {
            proformaServicioService.actualizarProformaServicio(id, proformaServicioDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al Proforma de Servicio" + e.getMessage());
        }
    }
    // Eliminar una proforma de servicio
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProformaServicio(@PathVariable Long id) {
        proformaServicioService.eliminarProformaServicio(id);
        return ResponseEntity.noContent().build();
    }
    //Buscar proformas de Servicio por Cliente(dni)
    @GetMapping("/cliente/{dni}")
    public List<ProformaServicioListaDTO> obtenerProformaServicioPorCliente(@PathVariable Long dni) {
        return proformaServicioService.obtenerProformaServicioPorCliente(dni);
    }
    //Endpoint para registrar pago
    @PostMapping("/{id}/pago")
    public ResponseEntity<String> registrarPago(@PathVariable Long id, @RequestPart(value="file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error, no hay archivo");
            }
            File tempFile = File.createTempFile("boleta_", ".pdf");
            file.transferTo(tempFile);
            proformaServicioService.registrarPago(id,tempFile);
            return ResponseEntity.status(HttpStatus.OK).body("Exito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar pago" + e.getMessage());
        }
    }

}

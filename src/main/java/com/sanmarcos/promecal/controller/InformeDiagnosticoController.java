package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.model.dto.InformeDiagnosticoDTO;
import com.sanmarcos.promecal.service.InformeDiagnosticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/informediagnostico")
public class InformeDiagnosticoController {
    @Autowired
    InformeDiagnosticoService informeDiagnosticoService;

    //Endpoint para guardar un informeDiagnostico
    @PostMapping
    public ResponseEntity<String> insertarInformeDiagnostico(@RequestPart("informe") InformeDiagnosticoDTO informeDiagnosticoDTO, @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                // Crear archivo temporal si se ha subido un archivo
                File tempFile = File.createTempFile("observaciones_", ".pdf");
                file.transferTo(tempFile);
                informeDiagnosticoService.insertarInformeDiagnostico(informeDiagnosticoDTO, tempFile);
            }else{
                informeDiagnosticoService.insertarInformeDiagnostico(informeDiagnosticoDTO, null);
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar informe diagnóstico: " + e.getMessage());
        }
    }
}

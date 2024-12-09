package com.sanmarcos.promecal.controller;
import com.sanmarcos.promecal.exception.InformeDiagnosticoException;
import com.sanmarcos.promecal.exception.TipoArchivoInvalidoException;
import com.sanmarcos.promecal.model.dto.InformeDiagnosticoDTO;
import com.sanmarcos.promecal.service.InformeDiagnosticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

@RestController
@RequestMapping("/api/informediagnostico")
public class InformeDiagnosticoController {
    @Autowired
    InformeDiagnosticoService informeDiagnosticoService;

    //Endpoint para guardar un informeDiagnostico
    @PostMapping
    public ResponseEntity<String> insertarInformeDiagnostico(@RequestPart("informe") InformeDiagnosticoDTO informeDiagnosticoDTO,
                                                             @RequestPart(value = "file", required = false) MultipartFile file) {
        // Verificar que el archivo sea un PDF
        if (!Objects.requireNonNull(file.getContentType()).equalsIgnoreCase("application/pdf")) {
            throw new TipoArchivoInvalidoException("El archivo debe ser un PDF");
        }
        if (file != null && !file.isEmpty()) {
            try {
                // Crear archivo temporal si se ha subido un archivo
                File tempFile = File.createTempFile("observaciones_", ".pdf");
                file.transferTo(tempFile);
                informeDiagnosticoService.insertarInformeDiagnostico(informeDiagnosticoDTO, tempFile);
            } catch (Exception e) {
                throw new InformeDiagnosticoException("Error al guardar el informe diagnóstico");
            }
        } else {
            try {
                informeDiagnosticoService.insertarInformeDiagnostico(informeDiagnosticoDTO, null);
            } catch (Exception e) {
                throw new InformeDiagnosticoException("Error al guardar el informe diagnóstico");
            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

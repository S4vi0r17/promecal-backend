package com.sanmarcos.promecal.model.dto;

import com.sanmarcos.promecal.model.entity.OrdenTrabajo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentoDTO {
    private OrdenTrabajo ordenTrabajo;
    private String nombre;
    private String rutaArchivo;
    private LocalDateTime fechaSubida;
}

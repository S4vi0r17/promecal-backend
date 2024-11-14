package com.sanmarcos.promecal.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class OrdenTrabajoHistorialDTO {
    private Long id;
    private Long ordenTrabajoId;
    private LocalDateTime fechaModificacion;
    private String campoModificado;
    private String valorAnterior;
    private String valorNuevo;
}

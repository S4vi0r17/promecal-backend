package com.sanmarcos.promecal.model.dto;

import com.sanmarcos.promecal.model.entity.OrdenTrabajo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InformeDiagnosticoDTO {
    private Long id;
    private Long id_ordenTrabajo;
    private String numeroSerie;
    private String estadoActual;
    private String problemasEncontrados;
    private String diagnosticoTecnico;
    private String recomendaciones;
    private String factibilidadReparacion;
    private Boolean equipoirreparable;
    private LocalDateTime fecha;
}

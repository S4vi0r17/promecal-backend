package com.sanmarcos.promecal.model.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProformaServicioDTO {
    private String codigo_ordentrabajo;
    private String detalleServicio;
    private Long precioServicio;
    private String tiempoEstimadoEntrega;
    private String condicionesContratacion;
    private String estadoPago;
    private LocalDateTime fecha;
}

package com.sanmarcos.promecal.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrdenTrabajoVistaDTO {

    private Long dni;

    private String nombrecompleto;

    private String codigo;

    private LocalDateTime fecha;

    private String descripcion;

    private String modelo;

    private String marca;

    private Boolean rajaduras;

    private Boolean manchas;

    private Boolean golpes;

    private String documentourl;
}

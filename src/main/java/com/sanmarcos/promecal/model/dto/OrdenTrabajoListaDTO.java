package com.sanmarcos.promecal.model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenTrabajoListaDTO {

    private Long id;

    private String codigo;

    private String dni;

    private String nombrecompleto;

    private LocalDateTime fecha;

    private String descripcion;

    private String modelo;

    private String marca;

    private Boolean rajaduras;

    private Boolean manchas;

    private Boolean golpes;
}

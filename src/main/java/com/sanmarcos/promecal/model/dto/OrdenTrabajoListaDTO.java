package com.sanmarcos.promecal.model.dto;

import com.sanmarcos.promecal.model.entity.Cliente;
import com.sanmarcos.promecal.model.entity.InformeDiagnostico;
import com.sanmarcos.promecal.model.entity.ProformaServicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenTrabajoListaDTO {

    private Long id;

    private String codigo;

    private Long dni;

    private String nombrecompleto;

    private LocalDateTime fecha;

    private String descripcion;

    private String modelo;

    private String marca;

    private Boolean rajaduras;

    private Boolean manchas;

    private Boolean golpes;
}

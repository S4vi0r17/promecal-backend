package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data // Lombok: Genera automáticamente getters, setters, toString, etc.
@Table(name = "informes_diagnostico")
public class InformeDiagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo_orden_trabajo", unique = true, length = 15, nullable = false)
    private String codigoOrdenTrabajo;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "estadoactual")
    private String estadoActual;

    @Column(name = "problemasencontrados")
    private String problemasEncontrados;

    @Column(name = "diagnosticotecnico")
    private String diagnosticoTecnico;

    @Column(name = "recomendaciones")
    private String recomendaciones;

    @Column(name = "factibilidadreparacion")
    private String factibilidadReparacion;

    @Column(name = "observacionesadicionales")
    private String observacionesAdicionales;

    @Column(name = "numeroserie")
    private String numeroSerie;

    @Column(name = "equipoirreparable")
    private Boolean equipoIrreparable;
}

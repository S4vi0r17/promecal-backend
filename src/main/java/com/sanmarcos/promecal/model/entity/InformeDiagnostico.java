package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "informes_diagnostico")
public class InformeDiagnostico {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ordentrabajo", referencedColumnName = "id", nullable = false)
    private OrdenTrabajo ordenTrabajo;

    private LocalDateTime fecha;
    private String estadoActual;
    private String problemasEncontrados;
    private String diagnosticoTecnico;
    private String recomendaciones;
    private String factibilidadReparacion;
    private String observacionesAdicionales;
    private String numeroSerie;
    private Boolean equipoirreparable;

}

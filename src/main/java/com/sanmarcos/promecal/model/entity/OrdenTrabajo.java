package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "ordenes_trabajo")
public class OrdenTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Cliente", nullable = false)
    private Cliente cliente;  // Relación con Cliente

    @Column(name = "Codigo", unique = true, length = 15)
    private String codigo;

    @Column(name = "Fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;

    @Column(name = "Descripcion", length = 50)
    private String descripcion;

    @Column(name = "Modelo", length = 10)
    private String modelo;

    @Column(name = "Marca", length = 10)
    private String marca;

    @Column(name = "Rajaduras")
    private Boolean rajaduras;

    @Column(name = "Manchas")
    private Boolean manchas;

    @Column(name = "Golpes")
    private Boolean golpes;

    @OneToOne(mappedBy = "ordenTrabajo", cascade = CascadeType.ALL)
    private Documento documento;

    @OneToOne(mappedBy = "ordenTrabajo", cascade = CascadeType.ALL)
    private InformeDiagnostico informeDiagnostico;

    @OneToOne(mappedBy = "ordenTrabajo")
    private ProformaServicio proformasServicio;

    @Column(name = "Estado")
    private Boolean estado;


}

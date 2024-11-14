package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ordenes_trabajo")
public class OrdenTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente", referencedColumnName = "id")
    private Cliente cliente;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "marca")
    private String marca;

    @Column(name = "rajaduras")
    private Boolean rajaduras;

    @Column(name = "manchas")
    private Boolean manchas;

    @Column(name = "golpes")
    private Boolean golpes;

    @Column(name = "estado")
    private Boolean estado;


}

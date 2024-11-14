package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "proformas_servicio")
@Data
public class ProformaServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordentrabajo", referencedColumnName = "id")
    private OrdenTrabajo ordenTrabajo;

    @Column(name = "detalleservicio")
    private String detalleServicio;

    @Column(name = "precioservicio", precision = 10, scale = 2)
    private Long precioServicio;

    @Column(name = "tiempoestimadoentrega")
    private String tiempoEstimadoEntrega;

    @Column(name = "condicionescontratacion")
    private String condicionesContratacion;

    @Column(name = "estadopago")
    private String estadoPago;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "boletaurl")
    private String boletaUrl;
}

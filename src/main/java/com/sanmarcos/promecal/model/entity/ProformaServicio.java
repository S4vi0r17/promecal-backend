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

    @OneToOne
    @JoinColumn(name = "OrdenTrabajo", nullable = false)
    private OrdenTrabajo ordenTrabajo;

    @Column(name = "DetalleServicio", length = 30)
    private String detalleServicio;

    @Column(name = "PrecioServicio", precision = 10, scale = 2)
    private Long precioServicio;

    @Column(name = "TiempoEstimadoEntrega", length = 10)
    private String tiempoEstimadoEntrega;

    @Column(name = "CondicionesContratacion", length = 50)
    private String condicionesContratacion;

    @Column(name = "EstadoPago", length = 10)
    private String estadoPago;

    private String boletaurl;

    @Column(name = "Fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;
}

package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "proformas_servicio")
@Data // Lombok: Genera automáticamente getters, setters, toString, etc.
public class ProformaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_orden_trabajo", referencedColumnName = "codigo", nullable = false, unique = true)
    private OrdenTrabajo ordenTrabajo;

    @Column(name = "detalleservicio", length = 30)
    private String detalleServicio;

    @Column(name = "precioservicio", precision = 10, scale = 2)
    private Long precioServicio;

    @Column(name = "tiempoestimadoentrega", length = 10)
    private String tiempoEstimadoEntrega;

    @Column(name = "condicionescontratacion", length = 50)
    private String condicionesContratacion;

    @Column(name = "estadopago", length = 10)
    private String estadoPago;

    @Column(name = "fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;

    @Column(name = "boletaurl", length = 50)
    private String boletaUrl;
}

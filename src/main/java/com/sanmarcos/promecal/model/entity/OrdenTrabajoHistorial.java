package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data // Lombok: Genera automáticamente getters, setters, toString, etc.
@Table(name = "orden_trabajo_historial")
public class OrdenTrabajoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_trabajo_id", referencedColumnName = "id", nullable = false)
    private OrdenTrabajo ordenTrabajo;

    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion;

    @Column(name = "campo_modificado", length = 255)
    private String campoModificado;

    @Column(name = "valor_anterior", length = 255)
    private String valorAnterior;

    @Column(name = "valor_nuevo", length = 255)
    private String valorNuevo;
}

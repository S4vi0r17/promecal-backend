package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data // Lombok: Genera automáticamente getters, setters, toString, etc.
@Table(name = "ordenes_trabajo", uniqueConstraints = {@UniqueConstraint(columnNames = {"codigo"})})
public class OrdenTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente", referencedColumnName = "id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", referencedColumnName = "id")
    private Documento documento;

    @Column(name = "codigo", unique = true, nullable = false, length = 15)
    private String codigo;

    @Column(name = "fecha", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;

    @Column(name = "descripcion", length = 50)
    private String descripcion;

    @Column(name = "modelo", length = 10)
    private String modelo;

    @Column(name = "marca", length = 10)
    private String marca;

    @Column(name = "rajaduras")
    private Boolean rajaduras;

    @Column(name = "manchas")
    private Boolean manchas;

    @Column(name = "golpes")
    private Boolean golpes;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
}

package com.sanmarcos.promecal.model.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "documentos")
@Data
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "orden_trabajo", referencedColumnName = "id", unique = true, nullable = false)
    private OrdenTrabajo ordenTrabajo;

    @Column(name = "nombre", length = 255)
    private String nombre;

    @Column(name = "ruta_archivo", length = 255)
    private String rutaArchivo;

    @Column(name = "fecha_subida", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaSubida;
}

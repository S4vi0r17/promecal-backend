package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data // Etiqueta DATA para generar automáticamente getters, setters, toString, etc.
@Table(name = "clientes", uniqueConstraints = {@UniqueConstraint(columnNames = {"dni"})})
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombrecompleto", length = 50)
    private String nombreCompleto;

    @Column(name = "dni", unique = true, length = 10, nullable = false)
    private String dni;

    @Column(name = "direccion", length = 30)
    private String direccion;

    @Column(name = "celular", length = 10)
    private String celular;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdenTrabajo> ordenesTrabajo;
}

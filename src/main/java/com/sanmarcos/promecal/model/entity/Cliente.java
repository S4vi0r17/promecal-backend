package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "clientes", uniqueConstraints = {@UniqueConstraint(columnNames = {"dni"})})
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombrecompleto")
    private String nombreCompleto;

    @Column(name = "dni", unique = true)
    private String dni;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "celular")
    private String celular;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrdenTrabajo> ordenesTrabajo;
}

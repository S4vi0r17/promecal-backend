package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "clientes", uniqueConstraints = {@UniqueConstraint(columnNames = {"dni"})})
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombrecompleto;
    private Long dni;
    private String direccion;
    private Long celular;


}

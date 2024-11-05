package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Generated;

@Entity
@Data
@Table(name="usuario")
public class Usuario {
    @Id
    @Generated
    private Long id;
    private String nombre_usuario;
    private String nombre_completo;
    private String correo_electronico;
    private String contrasena;
    private String rol;
}

package com.sanmarcos.promecal.model.dto;

import lombok.Data;

@Data
public class UsuarioListaDTO {
    private Long id;
    private String nombre_usuario;
    private String nombre_completo;
    private String rol;
}

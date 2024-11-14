package com.sanmarcos.promecal.model.dto;

import com.sanmarcos.promecal.model.entity.Rol;
import lombok.Data;

@Data
public class UsuarioVistaDTO {
    private String nombreusuario;
    private String nombrecompleto;
    private String correoelectronico;
    Rol rol;
}

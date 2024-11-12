package com.sanmarcos.promecal.model.dto;

import com.sanmarcos.promecal.model.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioListaDTO {
    private Long id;
    private String nombreusuario;
    private String nombrecompleto;
    private String correoelectronico;
    private String contrasena;
    Rol rol;
}

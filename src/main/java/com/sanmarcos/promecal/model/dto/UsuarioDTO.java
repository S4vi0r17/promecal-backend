package com.sanmarcos.promecal.model.dto;

import com.sanmarcos.promecal.model.entity.Rol;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String nombreusuario;
    private String nombrecompleto;
    @Email(message = "El correo electrónico debe ser válido")
    private String correoelectronico;
    private String contrasena;
    Rol rol;
}

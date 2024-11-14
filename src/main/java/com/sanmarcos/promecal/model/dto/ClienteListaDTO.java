package com.sanmarcos.promecal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteListaDTO {
    private Long id;
    private String nombrecompleto;
    private Long dni;
    private String direccion;
    private Long celular;
}

package com.sanmarcos.promecal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    //Endpoint para obtener lista de usuarios
    @GetMapping
    public String obtenerUsuarios() { return "asdasdad";}
}

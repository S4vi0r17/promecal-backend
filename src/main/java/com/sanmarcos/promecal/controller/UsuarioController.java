package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.model.dto.UsuarioListaDTO;
import com.sanmarcos.promecal.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    //Endpoint para obtener lista de usuarios
    @GetMapping
    public List<UsuarioListaDTO> obtenerUsuarios() { return usuarioService.obtenerUsuarios();}
}

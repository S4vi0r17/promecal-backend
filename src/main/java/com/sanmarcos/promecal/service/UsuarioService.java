package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.model.dto.UsuarioListaDTO;
import com.sanmarcos.promecal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    //Obtener lista de Usuarios
    public List<UsuarioListaDTO> obtenerUsuarios(){
        return usuarioRepository.findAll().stream().map(usuario ->{
            UsuarioListaDTO usuario1 = new UsuarioListaDTO();
            usuario1.setId(usuario.getId());
            usuario1.setRol(usuario.getRol());
            usuario1.setNombre_completo(usuario.getNombre_completo());
            usuario1.setNombre_usuario(usuario.getNombre_usuario());
            return usuario1;
        }).collect(Collectors.toList());
    }
}

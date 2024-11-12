package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.model.dto.UsuarioDTO;
import com.sanmarcos.promecal.model.dto.UsuarioListaDTO;
import com.sanmarcos.promecal.model.entity.Usuario;
import com.sanmarcos.promecal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Crear un nuevo usuario
    public Usuario insertarUsuario(UsuarioDTO usuariodto) {

        Usuario usuario = new Usuario();
        usuario.setNombreusuario(usuariodto.getNombreusuario());
        usuario.setContrasena(passwordEncoder.encode(usuariodto.getContrasena()));
        usuario.setCorreoelectronico(usuariodto.getCorreoelectronico());
        usuario.setNombrecompleto(usuariodto.getNombrecompleto());
        usuario.setRol(usuariodto.getRol());
        usuarioRepository.save(usuario);
        return usuario;
    }

    // Obtener todos los usuarios
    public List<UsuarioListaDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream().map(usuario -> {
            UsuarioListaDTO usuarioListaDTO = new UsuarioListaDTO();
            usuarioListaDTO.setNombreusuario(usuario.getNombreusuario());
            usuarioListaDTO.setNombrecompleto(usuario.getNombrecompleto());
            usuarioListaDTO.setCorreoelectronico(usuario.getCorreoelectronico());
            usuarioListaDTO.setRol(usuario.getRol());
            usuarioListaDTO.setId(usuario.getId());
            return usuarioListaDTO;
        }).collect(Collectors.toList());
    }

    // Obtener un usuario por ID
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombreusuario(usuario.getNombreusuario());
        usuarioDTO.setNombrecompleto(usuario.getNombrecompleto());
        usuarioDTO.setCorreoelectronico(usuario.getCorreoelectronico());
        usuarioDTO.setRol(usuario.getRol());
        return usuarioDTO;
    }

    //Metodo para actualizar un evento
    public void actualizarUsuario(Long id, UsuarioDTO usuarioDTO) throws IOException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombreusuario(usuarioDTO.getNombreusuario());
        usuario.setCorreoelectronico(usuarioDTO.getCorreoelectronico());
        usuario.setNombrecompleto(usuarioDTO.getNombrecompleto());
        usuario.setRol(usuarioDTO.getRol());
        usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

}

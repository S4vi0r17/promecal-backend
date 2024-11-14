package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.model.dto.UsuarioDTO;
import com.sanmarcos.promecal.model.dto.UsuarioListaDTO;
import com.sanmarcos.promecal.model.dto.UsuarioVistaDTO;
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
    public void insertarUsuario(UsuarioDTO usuariodto) {
        Usuario usuario = new Usuario();
        usuario.setNombreusuario(usuariodto.getNombreusuario());
        usuario.setContrasena(passwordEncoder.encode(usuariodto.getContrasena()));
        usuario.setCorreoelectronico(usuariodto.getCorreoelectronico());
        usuario.setNombrecompleto(usuariodto.getNombrecompleto());
        usuario.setRol(usuariodto.getRol());
        usuarioRepository.save(usuario);
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
    public UsuarioVistaDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        UsuarioVistaDTO usuarioVistaDTO= new UsuarioVistaDTO();
        usuarioVistaDTO.setNombrecompleto(usuario.getNombrecompleto());
        usuarioVistaDTO.setCorreoelectronico(usuario.getCorreoelectronico());
        usuarioVistaDTO.setRol(usuario.getRol());
        usuarioVistaDTO.setNombreusuario(usuario.getNombreusuario());
        return usuarioVistaDTO;
    }

    //Metodo para actualizar un usuario
    public void actualizarUsuario(Long id, UsuarioVistaDTO usuarioVistaDTO) throws IOException {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombreusuario(usuarioVistaDTO.getNombreusuario());
        usuario.setCorreoelectronico(usuarioVistaDTO.getCorreoelectronico());
        usuario.setNombrecompleto(usuarioVistaDTO.getNombrecompleto());
        usuario.setRol(usuarioVistaDTO.getRol());
        usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

}

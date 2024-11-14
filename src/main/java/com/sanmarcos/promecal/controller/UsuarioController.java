package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.model.dto.UsuarioDTO;
import com.sanmarcos.promecal.model.dto.UsuarioListaDTO;
import com.sanmarcos.promecal.model.dto.UsuarioVistaDTO;
import com.sanmarcos.promecal.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    //Endpoint para obtener la lista de usuario
    @GetMapping
    public List<UsuarioListaDTO> obtenerTodosLosUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    //Endpoint para guardar un usuario
    @PostMapping
    public ResponseEntity<String> insertarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            usuarioService.insertarUsuario(usuarioDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar usuario" + e.getMessage());
        }
    }

    //Endpoint para obtener los detalles un usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioVistaDTO> obtenerDetallesUsuario(@PathVariable Long id) {
        try {
            UsuarioVistaDTO usuarioVistaDTO=usuarioService.obtenerUsuarioPorId(id);
            return new ResponseEntity<>(usuarioVistaDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioVistaDTO usuarioVistaDTO) {
        try {
            usuarioService.actualizarUsuario(id, usuarioVistaDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el usuario" + e.getMessage());
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();  // Respuesta 204 No Content
    }

}

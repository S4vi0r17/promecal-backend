package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.exception.*;
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
    public ResponseEntity<List<UsuarioListaDTO>> obtenerTodosLosUsuarios() {
        try {
            List<UsuarioListaDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();

            // Validamos si no se encontraron usuarios
            if (usuarios.isEmpty()) {
                throw new UsuariosNoEncontradosException("No se encontraron usuarios en el sistema.");
            }

            return ResponseEntity.ok(usuarios);
        } catch (UsuariosNoEncontradosException e) {
            // Excepción personalizada para usuarios no encontrados
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Manejo de otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    //Endpoint para guardar un usuario
    @PostMapping
    public ResponseEntity<String> insertarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            usuarioService.insertarUsuario(usuarioDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UsuarioInvalidException e) {
            // Manejo de excepciones para campos inválidos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            // Manejo de errores generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar usuario: " + e.getMessage());
        }
    }


    //Endpoint para obtener los detalles un usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioVistaDTO> obtenerDetallesUsuario(@PathVariable Long id) {
        try {
            UsuarioVistaDTO usuarioVistaDTO = usuarioService.obtenerUsuarioPorId(id);
            return new ResponseEntity<>(usuarioVistaDTO, HttpStatus.OK);
        } catch (UsuarioNoEncontradoException e) {
            // Si se lanza la excepción personalizada, devolvemos una respuesta con el código 404 y el mensaje de error
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Para otros tipos de excepciones no esperadas
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioVistaDTO usuarioVistaDTO) {
        try {
            // Llamada al servicio para actualizar el usuario
            usuarioService.actualizarUsuario(id, usuarioVistaDTO);
            return new ResponseEntity<>("Usuario actualizado exitosamente", HttpStatus.OK);
        } catch (UsuarioNoEncontradoException e) {
            // Si se lanza la excepción personalizada para usuario no encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
        } catch (DatosInvalidosException e) {
            // Si se lanza la excepción personalizada para datos inválidos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos inválidos: " + e.getMessage());
        } catch (Exception e) {
            // Para otros tipos de errores no previstos
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        try {
            // Llamada al servicio para eliminar el usuario
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();  // Respuesta 204 No Content
        } catch (UsuarioNoEncontradoException e) {
            // Si se lanza la excepción personalizada para usuario no encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
        } catch (Exception e) {
            // Para otros tipos de errores no previstos
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el usuario: " + e.getMessage());
        }
    }


}

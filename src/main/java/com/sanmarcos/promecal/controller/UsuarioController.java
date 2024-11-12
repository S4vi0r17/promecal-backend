package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.model.dto.UsuarioDTO;
import com.sanmarcos.promecal.model.dto.UsuarioListaDTO;
import com.sanmarcos.promecal.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    //Endpoint para obtener la lista de usuario
    @GetMapping
    public List<UsuarioListaDTO> obtenerTodosLosUsuarios() {return usuarioService.obtenerTodosLosUsuarios();}
    //Endpoint para guardar un usuario
    @PostMapping
    public ResponseEntity<String> insertarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try{
            usuarioService.insertarUsuario(usuarioDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar usuario"+e.getMessage());
        }
    }
    //Endpoint para obtener los detalles un usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerDetallesUsuario(@PathVariable Long id){
        try{
            UsuarioDTO usuarioDTO = usuarioService.obtenerUsuarioPorId(id);
            return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
        }catch(RuntimeException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    // Endpoint paraActualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuariodto) {
        try{
            usuarioService.actualizarUsuario(id, usuariodto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el usuario"+e.getMessage());
        }
    }
    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();  // Respuesta 204 No Content
    }

}

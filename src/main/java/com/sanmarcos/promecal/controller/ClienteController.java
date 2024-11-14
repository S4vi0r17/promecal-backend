package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.model.dto.ClienteDTO;
import com.sanmarcos.promecal.model.dto.ClienteListaDTO;
import com.sanmarcos.promecal.model.dto.UsuarioDTO;
import com.sanmarcos.promecal.model.dto.UsuarioListaDTO;
import com.sanmarcos.promecal.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    //Endpoint para obtener la lista de clientes
    @GetMapping
    public List<ClienteListaDTO> obtenerTodosLosClientes() {
        return clienteService.obtenerTodosLosClientes();
    }

    //Endpoint para guardar un cliente
    @PostMapping
    public ResponseEntity<String> insertarCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            clienteService.insertarCliente(clienteDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar cliente" + e.getMessage());
        }
    }

    //Endpoint para obtener los detalles de un cliente por id
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerDetallesCliente(@PathVariable Long id) {
        try {
            ClienteDTO clienteDTO = clienteService.obtenerClientePorId(id);
            return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        try {
            clienteService.actualizarCliente(id, clienteDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el cliente" + e.getMessage());
        }
    }

    // Eliminar un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

}

package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.exception.ClienteNoEncontradoException;
import com.sanmarcos.promecal.exception.ClienteYaExisteException;
import com.sanmarcos.promecal.exception.DniDuplicadoException;
import com.sanmarcos.promecal.model.dto.ClienteDTO;
import com.sanmarcos.promecal.model.dto.ClienteListaDTO;
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
            // Intentar insertar el cliente
            clienteService.insertarCliente(clienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente registrado con éxito.");
        } catch (ClienteYaExisteException e) {
            // Manejar el caso en el que el cliente ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Manejar errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar cliente: " + e.getMessage());
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
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        try {
            clienteService.actualizarCliente(id, clienteDTO);
            return ResponseEntity.ok("Cliente actualizado con éxito.");
        } catch (ClienteNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DniDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (ClienteNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }

}

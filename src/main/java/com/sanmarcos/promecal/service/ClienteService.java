package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.model.dto.ClienteDTO;
import com.sanmarcos.promecal.model.dto.ClienteListaDTO;
import com.sanmarcos.promecal.model.entity.Cliente;
import com.sanmarcos.promecal.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    // Crear un nuevo cliente
    public void insertarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setDni(clienteDTO.getDni());
        cliente.setCelular(clienteDTO.getCelular());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setNombreCompleto(clienteDTO.getNombrecompleto());
        clienteRepository.save(cliente);
    }

    // Obtener todos los clientes
    public List<ClienteListaDTO> obtenerTodosLosClientes() {
        return clienteRepository.findAll().stream().map(cliente -> {
            ClienteListaDTO clientelistadto = new ClienteListaDTO();
            clientelistadto.setDni(cliente.getDni());
            clientelistadto.setCelular(cliente.getCelular());
            clientelistadto.setDireccion(cliente.getDireccion());
            clientelistadto.setNombrecompleto(cliente.getNombreCompleto());
            clientelistadto.setId(cliente.getId());
            return clientelistadto;
        }).collect(Collectors.toList());
    }

    // Obtener un cliente por ID
    public ClienteDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setDni(cliente.getDni());
        clienteDTO.setCelular(cliente.getCelular());
        clienteDTO.setDireccion(cliente.getDireccion());
        clienteDTO.setNombrecompleto(cliente.getNombreCompleto());
        return clienteDTO;
    }

    //Metodo para actualizar un cliente
    public void actualizarCliente(Long id, ClienteDTO clienteDTO) throws IOException {
        Cliente cliente= clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        cliente.setNombreCompleto(clienteDTO.getNombrecompleto());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setCelular(clienteDTO.getCelular());
        cliente.setDni(clienteDTO.getDni());
        clienteRepository.save(cliente);
    }

    // Eliminar cliente
    public void eliminarCliente(Long id) {
        Cliente cliente= clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteRepository.delete(cliente);
    }

}

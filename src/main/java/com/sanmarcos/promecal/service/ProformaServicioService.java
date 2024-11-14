package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.model.dto.ClienteDTO;
import com.sanmarcos.promecal.model.dto.ProformaServicioDTO;
import com.sanmarcos.promecal.model.dto.ProformaServicioListaDTO;
import com.sanmarcos.promecal.model.entity.Cliente;
import com.sanmarcos.promecal.model.entity.OrdenTrabajo;
import com.sanmarcos.promecal.model.entity.ProformaServicio;
import com.sanmarcos.promecal.repository.ClienteRepository;
import com.sanmarcos.promecal.repository.OrdenTrabajoRepository;
import com.sanmarcos.promecal.repository.ProformaServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProformaServicioService {
    @Autowired
    private ProformaServicioRepository proformaServicioRepository;
    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DriveService driveService;
    // Obtener todos las proformas de servicio
    public List<ProformaServicioListaDTO> obtenerTodosLasProformas() {
        return proformaServicioRepository.findAll().stream().map(proformaServicio -> {
            ProformaServicioListaDTO proformaServicioListaDTO = new ProformaServicioListaDTO();
            proformaServicioListaDTO.setDetalleServicio(proformaServicio.getDetalleServicio());
            proformaServicioListaDTO.setId(proformaServicio.getId());
            proformaServicioListaDTO.setFecha(proformaServicio.getFecha());
            proformaServicioListaDTO.setPrecioServicio(proformaServicio.getPrecioServicio());
            proformaServicioListaDTO.setEstadoPago(proformaServicio.getEstadoPago());
            proformaServicioListaDTO.setCondicionesContratacion(proformaServicio.getCondicionesContratacion());
            proformaServicioListaDTO.setCodigo_ordentrabajo(ordenTrabajoRepository.findById(proformaServicio.getOrdenTrabajo().getId()).get().getCodigo());
            proformaServicioListaDTO.setTiempoEstimadoEntrega(proformaServicio.getTiempoEstimadoEntrega());
            return proformaServicioListaDTO;
        }).collect(Collectors.toList());
    }

    public void insertarProformaServicio(ProformaServicioDTO proformaServicioDTO) {
        ProformaServicio proformaServicio = new ProformaServicio();
        proformaServicio.setFecha(proformaServicioDTO.getFecha());
        proformaServicio.setPrecioServicio(proformaServicioDTO.getPrecioServicio());
        proformaServicio.setEstadoPago(proformaServicioDTO.getEstadoPago());
        proformaServicio.setDetalleServicio(proformaServicioDTO.getDetalleServicio());
        proformaServicio.setCondicionesContratacion(proformaServicioDTO.getCondicionesContratacion());
        proformaServicio.setTiempoEstimadoEntrega(proformaServicioDTO.getTiempoEstimadoEntrega());
        proformaServicio.setOrdenTrabajo(ordenTrabajoRepository.findByCodigo(proformaServicioDTO.getCodigo_ordentrabajo()));
        proformaServicio.setBoletaUrl(null);
        proformaServicioRepository.save(proformaServicio);
    }

    public ProformaServicioDTO obtenerProformaServicioPorId(Long id) {
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id).orElseThrow(()-> new RuntimeException("Proforma de Servicio no encontrado"));
        ProformaServicioDTO proformaServicioDTO = new ProformaServicioDTO();
        proformaServicioDTO.setDetalleServicio(proformaServicio.getDetalleServicio());
        proformaServicioDTO.setPrecioServicio(proformaServicio.getPrecioServicio());
        proformaServicioDTO.setEstadoPago(proformaServicio.getEstadoPago());
        proformaServicioDTO.setFecha(proformaServicio.getFecha());
        proformaServicioDTO.setCondicionesContratacion(proformaServicio.getCondicionesContratacion());
        proformaServicioDTO.setTiempoEstimadoEntrega(proformaServicio.getTiempoEstimadoEntrega());
        proformaServicioDTO.setCodigo_ordentrabajo(proformaServicio.getOrdenTrabajo().getCodigo());
        return  proformaServicioDTO;
    }

    //Metodo para actualizar una proforma de servicio
    public void actualizarProformaServicio(Long id, ProformaServicioDTO proformaServicioDTO) throws IOException {
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id).orElseThrow(()-> new RuntimeException("Proforma de Servicio no encontrado"));
        proformaServicio.setFecha(proformaServicioDTO.getFecha());
        proformaServicio.setPrecioServicio(proformaServicioDTO.getPrecioServicio());
        proformaServicio.setEstadoPago(proformaServicioDTO.getEstadoPago());
        proformaServicio.setDetalleServicio(proformaServicioDTO.getDetalleServicio());
        proformaServicio.setTiempoEstimadoEntrega(proformaServicioDTO.getTiempoEstimadoEntrega());
        proformaServicio.setCondicionesContratacion(proformaServicioDTO.getCondicionesContratacion());
        proformaServicio.setBoletaUrl(null);
        proformaServicio.setOrdenTrabajo(ordenTrabajoRepository.findByCodigo(proformaServicioDTO.getCodigo_ordentrabajo()));
        proformaServicioRepository.save(proformaServicio);
    }

    public void eliminarProformaServicio(Long id) {
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id).orElseThrow(()-> new RuntimeException("Proforma de Servicio no Encontrada"));
        proformaServicioRepository.delete(proformaServicio);
    }

    public List<ProformaServicioListaDTO> obtenerProformaServicioPorCliente(Long dni) {
        // Buscar al cliente por su DNI
        Cliente cliente = clienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Obtener todas las ordenes de trabajo asociadas a ese cliente
        List<OrdenTrabajo> ordenesDeTrabajo = ordenTrabajoRepository.findByCliente(cliente);

        // Obtener las proformas de servicio asociadas a esas ordenes de trabajo
        return ordenesDeTrabajo.stream()
                .map(ordenTrabajo -> {
                    ProformaServicio proformaServicio = ordenTrabajo.ge; //
                    ProformaServicioListaDTO proformaServicioListaDTO = new ProformaServicioListaDTO();

                    // Mapear los campos de ProformaServicio al DTO
                    if (proformaServicio != null) { // Verificar si hay una Proforma asociada
                        proformaServicioListaDTO.setDetalleServicio(proformaServicio.getDetalleServicio());
                        proformaServicioListaDTO.setId(proformaServicio.getId());
                        proformaServicioListaDTO.setFecha(proformaServicio.getFecha());
                        proformaServicioListaDTO.setPrecioServicio(proformaServicio.getPrecioServicio());
                        proformaServicioListaDTO.setEstadoPago(proformaServicio.getEstadoPago());
                        proformaServicioListaDTO.setCondicionesContratacion(proformaServicio.getCondicionesContratacion());
                        proformaServicioListaDTO.setCodigo_ordentrabajo(ordenTrabajoRepository.findById(proformaServicio.getOrdenTrabajo().getId()).get().getCodigo());
                        proformaServicioListaDTO.setTiempoEstimadoEntrega(proformaServicio.getTiempoEstimadoEntrega());
                    }
                    return proformaServicioListaDTO;
                })
                .filter(dto -> dto.getId() != null) // Filtrar si no tiene una proforma asociada
                .collect(Collectors.toList());
    }

    public void registrarPago(Long id, File tempFile) {
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id).orElseThrow(()-> new RuntimeException("Proforma de Servicio no encontrado"));
        proformaServicio.setEstadoPago("PAGADO");
        proformaServicio.setBoletaurl(driveService.uploadPdfToDrive(tempFile,"proforma"));
    }
}

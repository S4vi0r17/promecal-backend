package com.sanmarcos.promecal.service;

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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProformaServicioService {

    private final ProformaServicioRepository proformaServicioRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final ClienteRepository clienteRepository;
    private final DriveService driveService;

    @Autowired
    public ProformaServicioService(ProformaServicioRepository proformaServicioRepository,
                                   OrdenTrabajoRepository ordenTrabajoRepository,
                                   ClienteRepository clienteRepository,
                                   DriveService driveService) {
        this.proformaServicioRepository = proformaServicioRepository;
        this.ordenTrabajoRepository = ordenTrabajoRepository;
        this.clienteRepository = clienteRepository;
        this.driveService = driveService;
    }

    // Obtener todas las proformas de servicio
    public List<ProformaServicioListaDTO> obtenerTodosLasProformas() {
        return proformaServicioRepository.findAll().stream().map(proformaServicio -> {
            ProformaServicioListaDTO proformaServicioListaDTO = new ProformaServicioListaDTO();
            proformaServicioListaDTO.setDetalleServicio(proformaServicio.getDetalleServicio());
            proformaServicioListaDTO.setId(proformaServicio.getId());
            proformaServicioListaDTO.setFecha(proformaServicio.getFecha());
            proformaServicioListaDTO.setPrecioServicio(proformaServicio.getPrecioServicio());
            proformaServicioListaDTO.setEstadoPago(proformaServicio.getEstadoPago());
            proformaServicioListaDTO.setCondicionesContratacion(proformaServicio.getCondicionesContratacion());
            proformaServicioListaDTO.setCodigo_ordentrabajo(proformaServicio.getOrdenTrabajo().getCodigo());
            proformaServicioListaDTO.setTiempoEstimadoEntrega(proformaServicio.getTiempoEstimadoEntrega());
            return proformaServicioListaDTO;
        }).collect(Collectors.toList());
    }

    // Insertar una nueva proforma de servicio
    public void insertarProformaServicio(ProformaServicioDTO proformaServicioDTO) {
        ProformaServicio proformaServicio = new ProformaServicio();
        proformaServicio.setFecha(proformaServicioDTO.getFecha());
        proformaServicio.setPrecioServicio(proformaServicioDTO.getPrecioServicio()); // Asegúrate de que sea `Double`
        proformaServicio.setEstadoPago(proformaServicioDTO.getEstadoPago());
        proformaServicio.setDetalleServicio(proformaServicioDTO.getDetalleServicio());
        proformaServicio.setCondicionesContratacion(proformaServicioDTO.getCondicionesContratacion());
        proformaServicio.setTiempoEstimadoEntrega(proformaServicioDTO.getTiempoEstimadoEntrega());

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findByCodigo(proformaServicioDTO.getCodigo_ordentrabajo())
                .orElseThrow(() -> new RuntimeException("Orden de Trabajo no encontrada"));
        proformaServicio.setOrdenTrabajo(ordenTrabajo);

        proformaServicio.setBoletaUrl(null);
        proformaServicioRepository.save(proformaServicio);
    }

    // Obtener proforma de servicio por ID
    public ProformaServicioDTO obtenerProformaServicioPorId(Long id) {
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proforma de Servicio no encontrada"));
        ProformaServicioDTO proformaServicioDTO = new ProformaServicioDTO();
        proformaServicioDTO.setDetalleServicio(proformaServicio.getDetalleServicio());
        proformaServicioDTO.setPrecioServicio(proformaServicio.getPrecioServicio());
        proformaServicioDTO.setEstadoPago(proformaServicio.getEstadoPago());
        proformaServicioDTO.setFecha(proformaServicio.getFecha());
        proformaServicioDTO.setCondicionesContratacion(proformaServicio.getCondicionesContratacion());
        proformaServicioDTO.setTiempoEstimadoEntrega(proformaServicio.getTiempoEstimadoEntrega());
        proformaServicioDTO.setCodigo_ordentrabajo(proformaServicio.getOrdenTrabajo().getCodigo());
        return proformaServicioDTO;
    }

    // Metodo para actualizar una proforma de servicio
    public void actualizarProformaServicio(Long id, ProformaServicioDTO proformaServicioDTO) throws IOException {
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proforma de Servicio no encontrada"));
        proformaServicio.setFecha(proformaServicioDTO.getFecha());
        proformaServicio.setPrecioServicio(proformaServicioDTO.getPrecioServicio());
        proformaServicio.setEstadoPago(proformaServicioDTO.getEstadoPago());
        proformaServicio.setDetalleServicio(proformaServicioDTO.getDetalleServicio());
        proformaServicio.setTiempoEstimadoEntrega(proformaServicioDTO.getTiempoEstimadoEntrega());
        proformaServicio.setCondicionesContratacion(proformaServicioDTO.getCondicionesContratacion());
        proformaServicio.setBoletaUrl(null);

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findByCodigo(proformaServicioDTO.getCodigo_ordentrabajo())
                .orElseThrow(() -> new RuntimeException("Orden de Trabajo no encontrada"));
        proformaServicio.setOrdenTrabajo(ordenTrabajo);

        proformaServicioRepository.save(proformaServicio);
    }

    // Eliminar una proforma de servicio
    public void eliminarProformaServicio(Long id) {
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proforma de Servicio no encontrada"));
        proformaServicioRepository.delete(proformaServicio);
    }

    // Obtener proformas de servicio por cliente
    public List<ProformaServicioListaDTO> obtenerProformaServicioPorCliente(String dni) {
        Cliente cliente = clienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<OrdenTrabajo> ordenesDeTrabajo = ordenTrabajoRepository.findByCliente(cliente);

        return ordenesDeTrabajo.stream()
                .map(ordenTrabajo -> {
                    ProformaServicio proformaServicio = proformaServicioRepository.findByOrdenTrabajo(ordenTrabajo);
                    ProformaServicioListaDTO proformaServicioListaDTO = new ProformaServicioListaDTO();

                    if (proformaServicio != null) {
                        proformaServicioListaDTO.setDetalleServicio(proformaServicio.getDetalleServicio());
                        proformaServicioListaDTO.setId(proformaServicio.getId());
                        proformaServicioListaDTO.setFecha(proformaServicio.getFecha());
                        proformaServicioListaDTO.setPrecioServicio(proformaServicio.getPrecioServicio());
                        proformaServicioListaDTO.setEstadoPago(proformaServicio.getEstadoPago());
                        proformaServicioListaDTO.setCondicionesContratacion(proformaServicio.getCondicionesContratacion());
                        proformaServicioListaDTO.setCodigo_ordentrabajo(proformaServicio.getOrdenTrabajo().getCodigo());
                        proformaServicioListaDTO.setTiempoEstimadoEntrega(proformaServicio.getTiempoEstimadoEntrega());
                    }
                    return proformaServicioListaDTO;
                })
                .filter(dto -> dto.getId() != null) // Filtrar si no tiene una proforma asociada
                .collect(Collectors.toList());
    }

    // Registrar el pago de una proforma
    public void registrarPago(Long id, File tempFile) {
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proforma de Servicio no encontrada"));
        proformaServicio.setEstadoPago("PAGADO");

        String boletaUrl = driveService.uploadPdfToDrive(tempFile, "proforma");
        proformaServicio.setBoletaUrl(boletaUrl);

        proformaServicioRepository.save(proformaServicio);
    }
}

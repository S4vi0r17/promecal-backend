package com.sanmarcos.promecal.service;
import com.sanmarcos.promecal.exception.*;
import com.sanmarcos.promecal.model.dto.OrdenTrabajoDTO;
import com.sanmarcos.promecal.model.dto.OrdenTrabajoHistorialDTO;
import com.sanmarcos.promecal.model.dto.OrdenTrabajoListaDTO;
import com.sanmarcos.promecal.model.dto.OrdenTrabajoVistaDTO;
import com.sanmarcos.promecal.model.entity.*;
import com.sanmarcos.promecal.repository.*;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrdenTrabajoService {
    @Autowired
    OrdenTrabajoRepository ordenTrabajoRepository;
    @Autowired
    OrdenTrabajoHistorialRepository ordenTrabajoHistorialRepository;
    @Autowired
    DriveService driveService;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    DocumentoRepository documentoRepository;
    public List<OrdenTrabajoListaDTO> obtenerOrdenesTrabajoConFiltros(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            String dni,
            String modelo,
            String codigo) {

        // Validar rango de fechas
        if (fechaInicio != null && fechaFin != null && fechaInicio.isAfter(fechaFin)) {
            throw new RangoFechaInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        // Filtro básico
        Specification<OrdenTrabajo> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("estado"), true));

        // Filtro por rango de fechas
        if (fechaInicio != null && fechaFin != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("fecha"), fechaInicio, fechaFin));
        }

        // Filtro por cliente DNI
        if (dni != null && !dni.isEmpty()) {
            Optional<Cliente> cliente = clienteRepository.findByDni(dni);
            if (cliente.isPresent()) {
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("cliente").get("id"), cliente.get().getId()));
            } else {
                throw new ClienteNoEncontradoException("El cliente con DNI " + dni + " no existe.");
            }
        }

        // Filtro por modelo
        if (modelo != null && !modelo.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("modelo"), modelo));
        }

        // Filtro por código
        if (codigo != null && !codigo.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("codigo"), codigo));
        }

        // Consultar y mapear a DTO
        return ordenTrabajoRepository.findAll(spec).stream()
                .map(this::convertirAListaDTO)
                .collect(Collectors.toList());
    }

    private OrdenTrabajoListaDTO convertirAListaDTO(OrdenTrabajo ordenTrabajo) {
        OrdenTrabajoListaDTO ordenTrabajoListaDTO=new OrdenTrabajoListaDTO();
        Cliente cliente = clienteRepository.findById(ordenTrabajo.getCliente().getId()).orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        ordenTrabajoListaDTO.setNombrecompleto(cliente.getNombreCompleto());
        ordenTrabajoListaDTO.setDni(cliente.getDni());
        ordenTrabajoListaDTO.setId(ordenTrabajo.getId());
        ordenTrabajoListaDTO.setDescripcion(ordenTrabajo.getDescripcion());
        ordenTrabajoListaDTO.setFecha(ordenTrabajo.getFecha());
        ordenTrabajoListaDTO.setCodigo(ordenTrabajo.getCodigo());
        ordenTrabajoListaDTO.setManchas(ordenTrabajo.getManchas());
        ordenTrabajoListaDTO.setGolpes(ordenTrabajo.getGolpes());
        ordenTrabajoListaDTO.setModelo(ordenTrabajo.getModelo());
        ordenTrabajoListaDTO.setRajaduras(ordenTrabajo.getRajaduras());
        ordenTrabajoListaDTO.setMarca(ordenTrabajo.getMarca());
        return ordenTrabajoListaDTO;
    }
    public String generarCodigo() {
        String prefijo = "ORD";
        // Generar un número aleatorio
        Random random = new Random();
        int numero = random.nextInt(100000);

        // Formatear el número con ceros a la izquierda
        String codigo = String.format("%s%05d", prefijo, numero);

        return codigo;
    }
    public void validarOrdenTrabajo(OrdenTrabajoDTO ordenTrabajoDTO, MultipartFile file) {
        if (ordenTrabajoDTO.getDni() == null || ordenTrabajoDTO.getDni().isEmpty()) {
            throw new ValidationException("El DNI del cliente es obligatorio.");
        }
        if (file != null && !Objects.equals(file.getContentType(), "application/pdf")) {
            throw new ValidationException("El archivo debe ser un documento PDF.");
        }
        if (ordenTrabajoDTO.getDescripcion().length() > 50) {
            throw new ValidationException("La descripción no puede exceder los 50 caracteres.");
        }
    }

    // Crear un nuevo orden trabajo
    public void insertarOrdenTrabajo(OrdenTrabajoDTO ordenTrabajoDTO, File file) {
        String codigo="";
        do {
            codigo = generarCodigo();
        } while (ordenTrabajoRepository.existsByCodigo(codigo));
        // Crear la nueva orden de trabajo
        OrdenTrabajo ordenTrabajo= new OrdenTrabajo();
        ordenTrabajo.setDescripcion(ordenTrabajoDTO.getDescripcion());
        ordenTrabajo.setFecha(ordenTrabajoDTO.getFecha());
        ordenTrabajo.setCodigo(codigo);
        ordenTrabajo.setManchas(ordenTrabajoDTO.getManchas());
        ordenTrabajo.setGolpes(ordenTrabajoDTO.getGolpes());
        ordenTrabajo.setModelo(ordenTrabajoDTO.getModelo());
        ordenTrabajo.setRajaduras(ordenTrabajoDTO.getRajaduras());
        ordenTrabajo.setMarca(ordenTrabajoDTO.getMarca());
        // Buscar cliente asociado
        Cliente cliente= clienteRepository.findByDni(ordenTrabajoDTO.getDni()).orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        ordenTrabajo.setCliente(cliente);
        //Setear por defecto
        ordenTrabajo.setEstado(true);
        // Crear y guardar documento
        Documento documento = new Documento();
        documento.setRutaArchivo(driveService.uploadPdfToDrive(file, "remision"));
        documento.setFechaSubida(LocalDateTime.now());
        documento.setNombre(file.getName());
        documentoRepository.save(documento);
        // Asociar el documento a la orden de trabajo
        ordenTrabajo.setDocumento(documento);
        // Guardar la orden de trabajo
        ordenTrabajoRepository.save(ordenTrabajo);
    }
    // Eliminar OrdenTrabajo
    public void eliminarOrdenTrabajo(Long id) {
        // Obtener la orden de trabajo
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new OrdenTrabajoNoEncontradaException("Orden de Trabajo con ID " + id + " no encontrada."));

        // Obtener el documento asociado
        Documento documento = ordenTrabajo.getDocumento();

        // Intentar eliminar el archivo en Drive si existe
        if (documento != null && documento.getRutaArchivo() != null) {
            try {
                String fileId = extraerFileIdDeUrl(documento.getRutaArchivo()); // Extrae el ID del archivo de la URL
                driveService.eliminarArchivoEnDrive(fileId); // Eliminar archivo en Drive
            } catch (Exception e) {
                throw new DocumentoEliminacionException("Error al eliminar el archivo asociado en Drive: " + documento.getRutaArchivo(), e);
            }
        }
        // Eliminar la orden de trabajo de la base de datos
        ordenTrabajoRepository.delete(ordenTrabajo);
    }

    // Metodo para extraer el ID del archivo desde la URL de Google Drive
    private String extraerFileIdDeUrl(String url) {
        // La URL tiene el formato "https://drive.google.com/uc?export=view&id=FILE_ID"
        String[] urlParts = url.split("id=");
        return urlParts.length > 1 ? urlParts[1] : null;
    }
    // Obtener un orden Trabajo por ID
    public OrdenTrabajoVistaDTO obtenerOrdenTrabajoPorId(Long id) {
        OrdenTrabajo ordenTrabajo=ordenTrabajoRepository.findById(id).orElseThrow(()-> new RuntimeException("Orden de Trabajo no encontrado"));
        OrdenTrabajoVistaDTO ordenTrabajoVistaDTO = new OrdenTrabajoVistaDTO();
        ordenTrabajoVistaDTO.setDescripcion(ordenTrabajo.getDescripcion());
        ordenTrabajoVistaDTO.setFecha(ordenTrabajo.getFecha());
        ordenTrabajoVistaDTO.setCodigo(ordenTrabajo.getCodigo());
        ordenTrabajoVistaDTO.setManchas(ordenTrabajo.getManchas());
        ordenTrabajoVistaDTO.setGolpes(ordenTrabajo.getGolpes());
        ordenTrabajoVistaDTO.setModelo(ordenTrabajo.getModelo());
        ordenTrabajoVistaDTO.setMarca(ordenTrabajo.getMarca());
        ordenTrabajoVistaDTO.setRajaduras(ordenTrabajo.getRajaduras());
        //Cliente
        ordenTrabajoVistaDTO.setDni(ordenTrabajo.getCliente().getDni());
        ordenTrabajoVistaDTO.setNombrecompleto(ordenTrabajo.getCliente().getNombreCompleto());
        //Documento
        ordenTrabajoVistaDTO.setDocumentourl(ordenTrabajo.getDocumento().getRutaArchivo());
        return ordenTrabajoVistaDTO;
    }
    // Metodo para actualizar una orden de trabajo
    public void actualizarOrdenTrabajo(Long id, OrdenTrabajoDTO ordenTrabajoDTO, File file) {
        // Obtener la orden de trabajo existente
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new OrdenTrabajoNoEncontradaException("Orden de Trabajo no encontrada"));

        try {
            // Compara los valores antiguos con los nuevos y guarda el historial de modificaciones si hay cambios
            if (!ordenTrabajo.getDescripcion().equals(ordenTrabajoDTO.getDescripcion())) {
                registrarHistorial(ordenTrabajo, "descripcion", ordenTrabajo.getDescripcion(), ordenTrabajoDTO.getDescripcion());
                ordenTrabajo.setDescripcion(ordenTrabajoDTO.getDescripcion());
            }

            if (!ordenTrabajo.getFecha().equals(ordenTrabajoDTO.getFecha())) {
                registrarHistorial(ordenTrabajo, "fecha", ordenTrabajo.getFecha(), ordenTrabajoDTO.getFecha());
                ordenTrabajo.setFecha(ordenTrabajoDTO.getFecha());
            }

            if (!ordenTrabajo.getManchas().equals(ordenTrabajoDTO.getManchas())) {
                registrarHistorial(ordenTrabajo, "manchas", ordenTrabajo.getManchas(), ordenTrabajoDTO.getManchas());
                ordenTrabajo.setManchas(ordenTrabajoDTO.getManchas());
            }

            if (!ordenTrabajo.getGolpes().equals(ordenTrabajoDTO.getGolpes())) {
                registrarHistorial(ordenTrabajo, "golpes", ordenTrabajo.getGolpes(), ordenTrabajoDTO.getGolpes());
                ordenTrabajo.setGolpes(ordenTrabajoDTO.getGolpes());
            }

            if (!ordenTrabajo.getModelo().equals(ordenTrabajoDTO.getModelo())) {
                registrarHistorial(ordenTrabajo, "modelo", ordenTrabajo.getModelo(), ordenTrabajoDTO.getModelo());
                ordenTrabajo.setModelo(ordenTrabajoDTO.getModelo());
            }

            if (!ordenTrabajo.getRajaduras().equals(ordenTrabajoDTO.getRajaduras())) {
                registrarHistorial(ordenTrabajo, "rajaduras", ordenTrabajo.getRajaduras(), ordenTrabajoDTO.getRajaduras());
                ordenTrabajo.setRajaduras(ordenTrabajoDTO.getRajaduras());
            }

            if (!ordenTrabajo.getMarca().equals(ordenTrabajoDTO.getMarca())) {
                registrarHistorial(ordenTrabajo, "marca", ordenTrabajo.getMarca(), ordenTrabajoDTO.getMarca());
                ordenTrabajo.setMarca(ordenTrabajoDTO.getMarca());
            }

            // Actualizar el cliente
            Cliente cliente = clienteRepository.findByDni(ordenTrabajoDTO.getDni())
                    .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado"));
            ordenTrabajo.setCliente(cliente);

            // Guardar la orden de trabajo con los nuevos valores
            ordenTrabajoRepository.save(ordenTrabajo);

        } catch (DataIntegrityViolationException e) {
            // Captura el error de clave duplicada
            throw new RuntimeException("Error al actualizar la orden de trabajo: El código ya está en uso.", e);
        } catch (RuntimeException e) {
            // Excepción para manejar cualquier otro error general
            throw new RuntimeException("Error al procesar la actualización de la orden de trabajo: " + e.getMessage(), e);
        }

        // Verificar si existe un documento asociado y actualizarlo si es necesario
        try {
            Documento documentoExistente = ordenTrabajo.getDocumento();
            if (documentoExistente == null || documentoExistente.getRutaArchivo() == null) {
                throw new DocumentoNoEncontradoException("No se encuentra el documento asociado a la orden de trabajo.");
            }

            // Solo procesar el archivo si se proporciona uno nuevo
            if (file != null && file.length() > 0) {
                String urlAnterior = documentoExistente.getRutaArchivo();
                String nombreArchivoNuevo = file.getName();

                // Subir el nuevo archivo a Google Drive y obtener la nueva URL
                String nuevaUrl = driveService.uploadPdfToDrive(file, "remision");

                // Registrar en el historial la URL anterior y la nueva
                registrarHistorial(ordenTrabajo, "documento", urlAnterior, nuevaUrl);

                // Actualizar la ruta del archivo en el documento
                documentoExistente.setRutaArchivo(nuevaUrl);
                documentoExistente.setFechaSubida(LocalDateTime.now());
                documentoExistente.setNombre(nombreArchivoNuevo);
            }

            // Guardar la orden de trabajo con el documento actualizado
            ordenTrabajoRepository.save(ordenTrabajo);

        } catch (RuntimeException e) {
            // Excepción para manejar errores con el documento
            throw new RuntimeException("Error al procesar el documento de la orden de trabajo: " + e.getMessage(), e);
        }
    }



    private void registrarHistorial(OrdenTrabajo ordenTrabajo, String campo, Object valorAnterior, Object valorNuevo) {
        // Considerar como vacío cualquier valor no significativo
        String valorAnteriorStr = valorAnterior != null ? valorAnterior.toString() : "null";
        String valorNuevoStr = valorNuevo != null ? valorNuevo.toString() : "null";

        // Solo registrar cambios si los valores son diferentes
        if (!valorAnteriorStr.equals(valorNuevoStr)) {
            OrdenTrabajoHistorial historial = new OrdenTrabajoHistorial();
            historial.setOrdenTrabajo(ordenTrabajo);
            historial.setFechaModificacion(LocalDateTime.now());
            historial.setCampoModificado(campo);
            historial.setValorAnterior(valorAnteriorStr);
            historial.setValorNuevo(valorNuevoStr);

            // Guardar el historial
            ordenTrabajoHistorialRepository.save(historial);
        }
    }
    public List<OrdenTrabajoHistorialDTO> obtenerHistorialDeOrden(Long ordenTrabajoId) {
        if (ordenTrabajoId <= 0) {
            throw new IllegalArgumentException("El ID de la orden de trabajo debe ser mayor a cero.");
        }

        List<OrdenTrabajoHistorial> historial = ordenTrabajoHistorialRepository.findByOrdenTrabajoId(ordenTrabajoId);
        if (historial.isEmpty()) {
            // Excepción personalizada si no se encuentra historial
            throw new HistorialNoEncontradoException("No se encontró historial para la Orden de Trabajo con ID " + ordenTrabajoId);
        }

        return historial.stream()
                .map(this::convertirAHistorialDTO)
                .collect(Collectors.toList());
    }


    private OrdenTrabajoHistorialDTO convertirAHistorialDTO(OrdenTrabajoHistorial historial) {
        OrdenTrabajoHistorialDTO historialDTO = new OrdenTrabajoHistorialDTO();
        historialDTO.setId(historial.getId());
        historialDTO.setOrdenTrabajoId(historial.getOrdenTrabajo().getId());
        historialDTO.setFechaModificacion(historial.getFechaModificacion());
        historialDTO.setCampoModificado(historial.getCampoModificado());
        historialDTO.setValorAnterior(historial.getValorAnterior());
        historialDTO.setValorNuevo(historial.getValorNuevo());

        return historialDTO;
    }


    public List<String> obtenerCodigos() {
        // Verificar si hay órdenes de trabajo
        List<OrdenTrabajo> ordenes = ordenTrabajoRepository.findAll();
        if (ordenes.isEmpty()) {
            throw new NoDataFoundException("No hay órdenes de trabajo disponibles.");
        }

        // Procesar y filtrar los códigos
        return ordenes.stream()
                .map(OrdenTrabajo::getCodigo) // Obtener el código
                .filter(codigo -> codigo != null && !codigo.isBlank()) // Validar códigos no nulos ni vacíos
                .collect(Collectors.toList());
    }

}

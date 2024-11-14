package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.model.dto.OrdenTrabajoDTO;
import com.sanmarcos.promecal.model.dto.OrdenTrabajoHistorialDTO;
import com.sanmarcos.promecal.model.dto.OrdenTrabajoListaDTO;
import com.sanmarcos.promecal.model.dto.OrdenTrabajoVistaDTO;
import com.sanmarcos.promecal.model.entity.Cliente;
import com.sanmarcos.promecal.model.entity.Documento;
import com.sanmarcos.promecal.model.entity.OrdenTrabajo;
import com.sanmarcos.promecal.model.entity.OrdenTrabajoHistorial;
import com.sanmarcos.promecal.repository.ClienteRepository;
import com.sanmarcos.promecal.repository.OrdenTrabajoHistorialRepository;
import com.sanmarcos.promecal.repository.OrdenTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
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

    //Obtener ordenes de trabajo
    public List<OrdenTrabajoListaDTO> obtenerOrdenesTrabajoConFiltros(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Long clienteId,
            String tipoEquipo,
            String numeroOrden) {

        Specification<OrdenTrabajo> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("estado"), true));  // Filtro para estado = true

        if (fechaInicio != null && fechaFin != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("fecha"), fechaInicio, fechaFin));
        }
        if (clienteId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("cliente").get("id"), clienteId));
        }
        if (tipoEquipo != null && !tipoEquipo.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("tipoEquipo"), tipoEquipo));
        }
        if (numeroOrden != null && !numeroOrden.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("codigo"), numeroOrden));
        }

        return ordenTrabajoRepository.findAll(spec).stream()
                .map(this::convertirAListaDTO)
                .collect(Collectors.toList());
    }

    private OrdenTrabajoListaDTO convertirAListaDTO(OrdenTrabajo ordenTrabajo) {
        OrdenTrabajoListaDTO ordenTrabajoListaDTO=new OrdenTrabajoListaDTO();
        Cliente cliente = clienteRepository.findById(ordenTrabajo.getCliente().getId()).orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        ordenTrabajoListaDTO.setNombrecompleto(cliente.getNombrecompleto());
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
    // Crear un nuevo orden trabajo
    public void insertarOrdenTrabajo(OrdenTrabajoDTO ordenTrabajoDTO, File file) {
        // Verificar si ya existe una orden de trabajo con el mismo código
        if (ordenTrabajoRepository.existsByCodigo(ordenTrabajoDTO.getCodigo())) {
            throw new RuntimeException(" El código de orden de trabajo ya está en uso.");
        }
        // Crear la nueva orden de trabajo
        OrdenTrabajo ordenTrabajo= new OrdenTrabajo();
        ordenTrabajo.setDescripcion(ordenTrabajoDTO.getDescripcion());
        ordenTrabajo.setFecha(ordenTrabajoDTO.getFecha());
        ordenTrabajo.setCodigo(ordenTrabajoDTO.getCodigo());
        ordenTrabajo.setManchas(ordenTrabajoDTO.getManchas());
        ordenTrabajo.setGolpes(ordenTrabajoDTO.getGolpes());
        ordenTrabajo.setModelo(ordenTrabajoDTO.getModelo());
        ordenTrabajo.setRajaduras(ordenTrabajoDTO.getRajaduras());
        ordenTrabajo.setMarca(ordenTrabajoDTO.getMarca());
        // Buscar cliente asociado
        Cliente cliente= clienteRepository.findById(ordenTrabajoDTO.getClienteId()).orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        ordenTrabajo.setCliente(cliente);
        //Crear documento
        Documento documento= new Documento();
        documento.setRutaArchivo(driveService.uploadPdfToDrive(file,"remision"));
        documento.setOrdenTrabajo(ordenTrabajo);
        documento.setFechaSubida(LocalDateTime.now());
        documento.setNombre(file.getName());
        ordenTrabajo.setDocumento(documento);
        //Setear por defecto
        ordenTrabajo.setEstado(true);
        ordenTrabajo.setInformeDiagnostico(null);
        ordenTrabajo.setProformasServicio(null);
        ordenTrabajoRepository.save(ordenTrabajo);
    }
    // Eliminar OrdenTrabajo
    public void eliminarOrdenTrabajo(Long id) {
        // Obtener la orden de trabajo
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de Trabajo no encontrado"));

        // Obtener el documento asociado (suponiendo que tienes la URL del archivo en Drive)
        Documento documento = ordenTrabajo.getDocumento();

        // Eliminar el archivo en Drive si existe
        if (documento != null && documento.getRutaArchivo() != null) {
            String fileId = extraerFileIdDeUrl(documento.getRutaArchivo()); // Extrae el ID del archivo de la URL
            driveService.eliminarArchivoEnDrive(fileId);// Llamar al servicio de Drive para eliminar el archivo
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
        ordenTrabajoVistaDTO.setNombrecompleto(ordenTrabajo.getCliente().getNombrecompleto());
        //Documento
        ordenTrabajoVistaDTO.setDocumentourl(ordenTrabajo.getDocumento().getRutaArchivo());
        return ordenTrabajoVistaDTO;
    }
    // Metodo para actualizar una orden de trabajo
    public void actualizarOrdenTrabajo(Long id, OrdenTrabajoDTO ordenTrabajoDTO, File file) {
        // Obtener la orden de trabajo
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de Trabajo no encontrada"));

        // Compara los valores antiguos con los nuevos y guarda el historial de modificaciones si hay cambios
        if (!ordenTrabajo.getDescripcion().equals(ordenTrabajoDTO.getDescripcion())) {
            registrarHistorial(ordenTrabajo, "descripcion", ordenTrabajo.getDescripcion(), ordenTrabajoDTO.getDescripcion());
            ordenTrabajo.setDescripcion(ordenTrabajoDTO.getDescripcion());
        }

        if (!ordenTrabajo.getFecha().equals(ordenTrabajoDTO.getFecha())) {
            registrarHistorial(ordenTrabajo, "fecha", ordenTrabajo.getFecha(), ordenTrabajoDTO.getFecha());
            ordenTrabajo.setFecha(ordenTrabajoDTO.getFecha());
        }

        if (!ordenTrabajo.getCodigo().equals(ordenTrabajoDTO.getCodigo())) {
            registrarHistorial(ordenTrabajo, "codigo", ordenTrabajo.getCodigo(), ordenTrabajoDTO.getCodigo());
            ordenTrabajo.setCodigo(ordenTrabajoDTO.getCodigo());
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
        ordenTrabajo.setCliente(clienteRepository.findById(ordenTrabajoDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));

        // Verificar si existe un documento asociado
        try {
            Documento documentoExistente = ordenTrabajo.getDocumento();

            // Verificar si el documento existe y tiene una ruta válida
            if (documentoExistente == null || documentoExistente.getRutaArchivo() == null) {
                throw new RuntimeException("No se encuentra el documento asociado a la orden de trabajo.");
            }

            // Solo procesar el archivo si se proporciona uno nuevo
            if (file != null) {
                // Registrar el cambio de archivo en el historial (registrar la URL antigua y la nueva)
                String urlAnterior = documentoExistente.getRutaArchivo();
                String nombreArchivoNuevo = file.getName();

                // Subir el nuevo archivo a Google Drive y obtener la nueva URL
                String nuevaUrl = driveService.uploadPdfToDrive(file,"remision");  // Subir el archivo

                // Registrar en el historial la URL anterior y la nueva
                registrarHistorial(ordenTrabajo, "documento", urlAnterior, nuevaUrl);

                // Actualizar la ruta del archivo en el documento
                documentoExistente.setRutaArchivo(nuevaUrl);  // Actualizar la URL del archivo
                documentoExistente.setFechaSubida(LocalDateTime.now());
                documentoExistente.setNombre(nombreArchivoNuevo);
            }

            // Guardar la orden de trabajo con el documento actualizado
            ordenTrabajoRepository.save(ordenTrabajo);

        } catch (RuntimeException e) {
            // Si el documento no existe o tiene problemas, se lanza una excepción personalizada
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
        List<OrdenTrabajoHistorial> historial = ordenTrabajoHistorialRepository.findByOrdenTrabajoId(ordenTrabajoId);
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


}

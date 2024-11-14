package com.sanmarcos.promecal.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.sanmarcos.promecal.model.dto.InformeDiagnosticoDTO;
import java.io.IOException;
import com.sanmarcos.promecal.model.entity.InformeDiagnostico;
import com.sanmarcos.promecal.repository.InformeDiagnosticoRepository;
import com.sanmarcos.promecal.repository.OrdenTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.io.FileNotFoundException;

@Service
public class InformeDiagnosticoService {
    @Autowired
    InformeDiagnosticoRepository informeDiagnosticoRepository;
    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;
    @Autowired
    private DriveService driveService;
    @Autowired
    private EmailService emailService;

    public void insertarInformeDiagnostico(InformeDiagnosticoDTO informeDiagnosticoDTO, File file) throws IOException {
        //Crear el Informe Diagnostico
        InformeDiagnostico informeDiagnostico = new InformeDiagnostico();
        informeDiagnostico.setFecha(informeDiagnosticoDTO.getFecha());
        informeDiagnostico.setEstadoActual(informeDiagnosticoDTO.getEstadoActual());
        informeDiagnostico.setNumeroSerie(informeDiagnosticoDTO.getNumeroSerie());
        informeDiagnostico.setProblemasEncontrados(informeDiagnosticoDTO.getProblemasEncontrados());
        informeDiagnostico.setFactibilidadReparacion(informeDiagnosticoDTO.getFactibilidadReparacion());
        informeDiagnostico.setRecomendaciones(informeDiagnosticoDTO.getRecomendaciones());
        informeDiagnostico.setDiagnosticoTecnico(informeDiagnosticoDTO.getDiagnosticoTecnico());
        informeDiagnostico.setCodigoOrdenTrabajo(ordenTrabajoRepository.findById(informeDiagnosticoDTO.getId_ordenTrabajo()).orElseThrow(()-> new RuntimeException("Orden Trabajo no encontrado")).getCodigo());
        informeDiagnostico.setEquipoIrreparable(informeDiagnosticoDTO.getEquipoirreparable());
        //Subir el archivo si hay
        if(file==null){
            informeDiagnostico.setObservacionesAdicionales("No hay");
        }else{
            informeDiagnostico.setObservacionesAdicionales(driveService.uploadPdfToDrive(file,"observaciones"));
        }
        // Generar el PDF del informe
        File pdfcreado= generarPDF(informeDiagnostico);

        informeDiagnosticoRepository.save(informeDiagnostico);
        String name=driveService.uploadPdfToDrive(pdfcreado,"informe");
        System.out.println(name);
        emailService.enviarCorreo("Jefferson.asencios@unmsm.edu.pe",name);
        // Borrar el archivo después de enviarlo por correo
        if (pdfcreado.exists()) {
            boolean deleted = pdfcreado.delete();
            if (deleted) {
                System.out.println("El archivo PDF se ha borrado correctamente.");
            } else {
                System.out.println("No se pudo borrar el archivo PDF.");
            }
        }
        ;
    }

    public File generarPDF(InformeDiagnostico informeDiagnostico) throws IOException {
        // Crear un archivo temporal
        File tempFile = File.createTempFile("informe_", ".pdf");

        // Obtener el nombre del archivo temporal
        String fileName = tempFile.getName(); // Esto te dará algo como "informe_12345.pdf"

        // Extraer la parte entre "informe_" y ".pdf"
        String nameWithoutPrefixAndSuffix = fileName.substring("informe_".length(), fileName.lastIndexOf(".pdf"));

        File file = new File("informe_" + nameWithoutPrefixAndSuffix+ ".pdf");

        try (PdfWriter pdfWriter = new PdfWriter(file)) {
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            // Agregar título al PDF
            document.add(new Paragraph("Informe Diagnóstico").setBold().setFontSize(16));

            document.add(new Paragraph("Fecha: " + informeDiagnostico.getFecha()));
            document.add(new Paragraph("Estado Actual: " + informeDiagnostico.getEstadoActual()));
            document.add(new Paragraph("Número de Serie: " + informeDiagnostico.getNumeroSerie()));
            document.add(new Paragraph("Factibilidad de Reparación: " + informeDiagnostico.getFactibilidadReparacion()));
            document.add(new Paragraph("Recomendaciones: " + informeDiagnostico.getRecomendaciones()));
            document.add(new Paragraph("Diagnóstico Técnico: " + informeDiagnostico.getDiagnosticoTecnico()));
            document.add(new Paragraph("Observaciones Adicionales: " + informeDiagnostico.getObservacionesAdicionales()));

            // Cerrar el documento
            document.close();

            System.out.println("PDF creado exitosamente");

        } catch (FileNotFoundException ex) {
            System.out.println("Error al crear el archivo: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error al generar el PDF: " + ex.getMessage());
        }

        return file; // Retorna el archivo creado
    }
}

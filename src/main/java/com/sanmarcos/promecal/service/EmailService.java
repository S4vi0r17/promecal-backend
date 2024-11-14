package com.sanmarcos.promecal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    public void enviarCorreo(String destinatario, String urlInformeDiagnostico) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Informe de Diagnóstico Adjunto");
        mensaje.setText(String.format(
                "Buenas, señor Ejecutivo de Ventas.\n\nLe adjunto el link del informe de diagnóstico: %s",
                urlInformeDiagnostico
        ));
        mailSender.send(mensaje);
    }
}

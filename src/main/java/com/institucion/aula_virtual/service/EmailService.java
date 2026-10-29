package com.institucion.aula_virtual.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoRecuperacion(String destinatario, String enlace) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Recuperación de Contraseña - Aula Túpac Amaru");
        mensaje.setText("""
                        Hola,
                        
                        Has solicitado restablecer tu contrase\u00f1a.
                        Haz clic en el siguiente enlace para crear una nueva:
                        
                        """ +
                enlace + "\n\n" +  // <-- AQUÍ ESTÁ LA MAGIA: el enlace va solito
                "Si no solicitaste esto, ignora este mensaje.\n\n" +
                "Atentamente,\n" +
                "El equipo de Aula Túpac Amaru.");
        
        mailSender.send(mensaje);
    }
}
package com.fpemp.gestionfp.service;

import com.fpemp.gestionfp.model.Practica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

/**
 * @author Hafdala Mehdi Sidi
 * Servicio para el envío de emails automáticos.
 * Se usa cuando se crea una nueva práctica para notificar al alumno
 * con los datos de la empresa y las fechas de inicio y fin.
 */
@Service
public class ServicioEmail {

    @Autowired
    private JavaMailSender enviadorEmail;

    /**
     * Envía un email al alumno cuando se le asigna una práctica.
     * El email incluye el nombre de la empresa, el tutor y las fechas.
     */
    public void enviarEmailPractica(Practica practica) {

        // Formato de fecha en español para el email
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Construimos el mensaje de texto del email
        String mensaje = "Hola " + practica.getAlumno().getNombre() + ",\n\n"
                + "Te informamos de que has sido asignado/a a una práctica en empresa.\n\n"
                + "Datos de la práctica:\n"
                + "- Empresa: " + practica.getEmpresa().getNombre() + "\n"
                + "- Tutor laboral: " + practica.getEmpresa().getNombreTutor() + "\n"
                + "- Email del tutor: " + practica.getEmpresa().getEmailTutor() + "\n"
                + "- Fecha de inicio: " + practica.getFechaInicio().format(formato) + "\n"
                + "- Fecha de fin: " + practica.getFechaFin().format(formato) + "\n\n"
                + "Cualquier duda contacta con tu profesor.\n\n"
                + "Un saludo,\n"
                + "Gestión Formación en Empresa";

        // Creamos el email con destinatario, asunto y cuerpo
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(practica.getAlumno().getEmail());
        email.setSubject("Asignación de Prácticas en Empresa");
        email.setText(mensaje);

        // Enviamos el email
        enviadorEmail.send(email);
    }
}


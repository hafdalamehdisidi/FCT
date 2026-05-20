package com.fpemp.gestionfp.service;

import com.fpemp.gestionfp.model.Practica;
import com.fpemp.gestionfp.repository.PracticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Hafdala Mehdi Sidi
 * Servicio para la gestión de prácticas.
 * Cuando se crea una práctica se envía automáticamente un email al alumno.
 * Accesible para todos los profesores.
 */

@Service
public class ServicioPractica {

    @Autowired
    private PracticaRepository repositorioPractica;

    @Autowired
    private ServicioEmail servicioEmail;

    // Devuelve todas las prácticas de la base de datos
    public List<Practica> obtenerTodas() {
        return repositorioPractica.findAll();
    }

    // Busca una práctica por su id, lanza excepción si no existe
    public Practica obtenerPorId(Long id) {
        return repositorioPractica.findById(id)
                .orElseThrow(() -> new RuntimeException("Práctica no encontrada con id: " + id));
    }

    // Guarda una práctica nueva y envía el email al alumno automáticamente
    public void guardar(Practica practica) {
        repositorioPractica.save(practica);

        // Enviamos el email al alumno tras guardar la práctica
        try {
            servicioEmail.enviarEmailPractica(practica);
        } catch (Exception e) {
            // Si falla el email no interrumpimos el proceso
            System.out.println("Aviso: no se pudo enviar el email: " + e.getMessage());
        }
    }

    // Actualiza los datos de una práctica existente
    // Al editar no se vuelve a enviar el email
    public void actualizar(Long id, Practica practicaActualizada) {
        Practica practicaExistente = obtenerPorId(id);
        practicaExistente.setAlumno(practicaActualizada.getAlumno());
        practicaExistente.setEmpresa(practicaActualizada.getEmpresa());
        practicaExistente.setFechaInicio(practicaActualizada.getFechaInicio());
        practicaExistente.setFechaFin(practicaActualizada.getFechaFin());
        practicaExistente.setComentarios(practicaActualizada.getComentarios());
        repositorioPractica.save(practicaExistente);
    }

    // Elimina una práctica por su id
    public void eliminar(Long id) {
        repositorioPractica.deleteById(id);
    }
}

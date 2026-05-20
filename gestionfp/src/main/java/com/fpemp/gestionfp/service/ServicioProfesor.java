package com.fpemp.gestionfp.service;

import com.fpemp.gestionfp.model.Profesor;
import com.fpemp.gestionfp.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Hafdala Mehdi Sidi
 * Servicio para la gestión de profesores.
 * Contiene la lógica de negocio: crear, editar, eliminar y listar profesores.
 * El controlador llama a este servicio, y este llama al repositorio.
 * Así separamos la lógica de negocio de la capa web (MVC).
 */
@Service
public class ServicioProfesor {

    @Autowired
    private ProfesorRepository repositorioProfesor;

    @Autowired
    private PasswordEncoder codificadorPassword;

    // Devuelve todos los profesores de la base de datos
    public List<Profesor> obtenerTodos() {
        return repositorioProfesor.findAll();
    }

    // Busca un profesor por su id, lanza excepción si no existe
    public Profesor obtenerPorId(Long id) {
        return repositorioProfesor.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + id));
    }

    // Guarda un profesor nuevo encriptando su contraseña antes de guardarlo
    public void guardar(Profesor profesor) {
        // Encriptamos la contraseña antes de guardarla en la base de datos
        profesor.setPassword(codificadorPassword.encode(profesor.getPassword()));
        repositorioProfesor.save(profesor);
    }

    // Actualiza los datos de un profesor existente
    // Si la contraseña viene vacía, mantenemos la que ya tenía
    public void actualizar(Long id, Profesor profesorActualizado) {
        Profesor profesorExistente = obtenerPorId(id);

        profesorExistente.setNombre(profesorActualizado.getNombre());
        profesorExistente.setApellidos(profesorActualizado.getApellidos());
        profesorExistente.setEmail(profesorActualizado.getEmail());
        profesorExistente.setDirectiva(profesorActualizado.isDirectiva());

        // Solo actualizamos la contraseña si se ha introducido una nueva
        if (profesorActualizado.getPassword() != null &&
                !profesorActualizado.getPassword().isEmpty()) {
            profesorExistente.setPassword(
                    codificadorPassword.encode(profesorActualizado.getPassword()));
        }

        repositorioProfesor.save(profesorExistente);
    }

    // Elimina un profesor por su id
    // No se puede eliminar si es el único miembro de la Directiva
    // ni si es el propio usuario autenticado
    public void eliminar(Long id) {
        Profesor profesor = obtenerPorId(id);

        String emailAutenticado = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        if (profesor.getEmail().equals(emailAutenticado)) {
            throw new RuntimeException("No puedes eliminar tu propio usuario");
        }

        if (profesor.isDirectiva() && repositorioProfesor.countByDirectiva(true) <= 1) {
            throw new RuntimeException(
                    "No se puede eliminar el único profesor de la Directiva");
        }

        repositorioProfesor.deleteById(id);
    }

    // Comprueba si ya existe un profesor con ese email
    public boolean existeEmail(String email) {
        return repositorioProfesor.findByEmail(email).isPresent();
    }
}

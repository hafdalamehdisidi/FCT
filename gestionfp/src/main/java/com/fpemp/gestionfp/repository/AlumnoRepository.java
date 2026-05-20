package com.fpemp.gestionfp.repository;

import com.fpemp.gestionfp.model.Alumno;
import com.fpemp.gestionfp.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author Hafdala Mehdi Sidi
 * Repositorio para la entidad Alumno.
 */
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    // Devuelve todos los alumnos de un curso concreto
    // Se usa para mostrar alumnos filtrados por curso
    List<Alumno> findByCurso(Curso curso);

    // Comprueba si ya existe un alumno con ese email (para evitar duplicados)
    boolean existsByEmail(String email);
}

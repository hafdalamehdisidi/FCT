package com.fpemp.gestionfp.repository;

import com.fpemp.gestionfp.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Hafdala Mehdi Sidi
 * Repositorio para la entidad Curso.
 */
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Comprueba si ya existe un curso con ese nombre (para evitar duplicados)
    boolean existsByNombre(String nombre);
}

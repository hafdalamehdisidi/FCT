package com.fpemp.gestionfp.repository;

import com.fpemp.gestionfp.model.Practica;
import com.fpemp.gestionfp.model.Empresa;
import com.fpemp.gestionfp.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


/**
 * @author Hafdala Mehdi Sidi
 * Repositorio para la entidad Practica.
 */
@Repository
public interface PracticaRepository extends JpaRepository<Practica, Long> {

    // Devuelve todas las prácticas de un alumno concreto
    List<Practica> findByAlumnoId(Long alumnoId);

    // Cuenta cuántos alumnos tiene asignados cada empresa (para estadísticas)
    @Query("SELECT p.empresa, COUNT(p) FROM Practica p GROUP BY p.empresa")
    List<Object[]> countAlumnosPorEmpresa();

    // Cuenta cuántos alumnos hacen prácticas por cada curso (para estadísticas)
    @Query("SELECT p.alumno.curso, COUNT(p) FROM Practica p GROUP BY p.alumno.curso")
    List<Object[]> countAlumnosPorCurso();
}

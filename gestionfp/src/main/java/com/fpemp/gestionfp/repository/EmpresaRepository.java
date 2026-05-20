package com.fpemp.gestionfp.repository;

import com.fpemp.gestionfp.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Hafdala Mehdi Sidi
 * Repositorio para la entidad Empresa.
 */
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    // Comprueba si ya existe una empresa con ese nombre
    boolean existsByNombre(String nombre);
}

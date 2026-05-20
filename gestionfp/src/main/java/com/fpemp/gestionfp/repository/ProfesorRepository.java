package com.fpemp.gestionfp.repository;

import com.fpemp.gestionfp.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * @author Hafdala Mehdi Sidi
 * Repositorio para la entidad Profesor.
 * Spring Data JPA genera de forma automatrica las consultas SQL básicas para realizar las operaciones CRUD.
 */
@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    // Spring genera automáticamente el SQL para buscar por email
    // Se usa en el login para identificar al profesor
    Optional<Profesor> findByEmail(String email);
}

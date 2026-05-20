package com.fpemp.gestionfp.security;

import com.fpemp.gestionfp.model.Profesor;
import com.fpemp.gestionfp.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Hafdala Mehdi Sidi
 * Esta clase conecta Spring Security con nuestra base de datos.
 * Cuando un profesor intenta hacer login, Spring Security llama a este
 * metodo para buscar en la base de datos si existe un profesor con ese email y su contraseña.
 */
@Service
public class ServicioUsuarioImpl implements UserDetailsService {

    @Autowired
    private ProfesorRepository repositorioProfesor;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Buscamos al profesor por email en la base de datos
        Profesor profesor = repositorioProfesor.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No existe el profesor con email: " + email));

        // Asignamos el rol según si pertenece a la Directiva o no
        String rol = profesor.isDirectiva() ? "DIRECTIVA" : "PROFESOR";

        // Devolvemos los datos que Spring Security necesita para autenticar
        return User.builder()
                .username(profesor.getEmail())
                .password(profesor.getPassword())
                .roles(rol)
                .build();
    }
}

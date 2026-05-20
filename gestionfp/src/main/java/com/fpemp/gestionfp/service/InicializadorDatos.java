package com.fpemp.gestionfp.service;

import com.fpemp.gestionfp.model.Profesor;
import com.fpemp.gestionfp.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Hafdala Mehdi Sidi
Esta clase se ejecuta automáticamente al arrancar la aplicación.
 * Crea un profesor de la Directiva por defecto si no existe ninguno,
 * para poder hacer login desde el primer momento.
 */
@Component
public class InicializadorDatos implements CommandLineRunner {

    @Autowired
    private ProfesorRepository repositorioProfesor;

    @Autowired
    private PasswordEncoder codificadorPassword;

    @Override
    public void run(String... args) throws Exception {

        // Solo creamos el profesor inicial si la tabla está vacía
        if (repositorioProfesor.count() == 0) {

            Profesor director = new Profesor();
            director.setNombre("Admin");
            director.setApellidos("Directiva");
            director.setEmail("admin@fpemp.com");
            // La contraseña se encripta con BCrypt antes de guardarla
            director.setPassword(codificadorPassword.encode("admin123"));
            director.setDirectiva(true);

            repositorioProfesor.save(director);
            System.out.println("Profesor inicial creado: admin@fpemp.com / admin123");
        }
    }
}



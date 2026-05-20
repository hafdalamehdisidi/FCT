package com.fpemp.gestionfp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Hafdala Mehdi Sidi
*Controlador para las páginas de login e inicio.
 * Gestiona la navegación básica de la aplicación.
 */
@Controller
public class ControladorInicio {

    // Muestra la página de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Muestra la página de inicio después del login
    @GetMapping("/inicio")
    public String mostrarInicio() {
        return "inicio";
    }
}

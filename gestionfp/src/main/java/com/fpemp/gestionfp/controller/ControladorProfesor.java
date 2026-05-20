package com.fpemp.gestionfp.controller;

import com.fpemp.gestionfp.model.Profesor;
import com.fpemp.gestionfp.service.ServicioProfesor;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Hafdala Mehdi Sidi
 * Controlador para la gestión de profesores.
 * Solo accesible para profesores de la Directiva (configurado en SecurityConfig).
 * Gestiona las peticiones HTTP y delega la lógica en ServicioProfesor.
 */
@Controller
@RequestMapping("/profesores")
public class ControladorProfesor {

    @Autowired
    private ServicioProfesor servicioProfesor;

    // Muestra la lista de todos los profesores
    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("profesores", servicioProfesor.obtenerTodos());
        return "profesores/lista";
    }

    // Muestra el formulario para crear un nuevo profesor
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model modelo) {
        modelo.addAttribute("profesor", new Profesor());
        return "profesores/formulario";
    }

    // Recibe los datos del formulario y guarda el nuevo profesor
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("profesor") Profesor profesor,
                          BindingResult resultado,
                          RedirectAttributes atributos) {

        // Si hay errores de validación volvemos al formulario
        if (resultado.hasErrors()) {
            return "profesores/formulario";
        }

        // Al crear, la contraseña es obligatoria
        if (profesor.getPassword() == null || profesor.getPassword().isBlank()) {
            resultado.rejectValue("password", "error.password", "La contraseña es obligatoria");
            return "profesores/formulario";
        }

        // Comprobamos que el email no esté ya registrado
        if (servicioProfesor.existeEmail(profesor.getEmail())) {
            resultado.rejectValue("email", "error.email", "Este email ya está registrado");
            return "profesores/formulario";
        }

        servicioProfesor.guardar(profesor);
        atributos.addFlashAttribute("mensaje", "Profesor creado correctamente");
        return "redirect:/profesores";
    }

    // Muestra el formulario para editar un profesor existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("profesor", servicioProfesor.obtenerPorId(id));
        return "profesores/formulario";
    }

    // Recibe los datos del formulario y actualiza el profesor
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("profesor") Profesor profesor,
                             BindingResult resultado,
                             RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            return "profesores/formulario";
        }

        servicioProfesor.actualizar(id, profesor);
        atributos.addFlashAttribute("mensaje", "Profesor actualizado correctamente");
        return "redirect:/profesores";
    }

    // Elimina un profesor por su id
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            servicioProfesor.eliminar(id);
            atributos.addFlashAttribute("mensaje", "Profesor eliminado correctamente");
        } catch (Exception e) {
            // Si el profesor tiene datos asociados no se puede eliminar
            atributos.addFlashAttribute("error", "No se puede eliminar este profesor");
        }
        return "redirect:/profesores";
    }
}

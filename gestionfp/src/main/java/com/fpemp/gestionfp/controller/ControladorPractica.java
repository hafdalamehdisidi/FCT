package com.fpemp.gestionfp.controller;

import com.fpemp.gestionfp.model.Practica;
import com.fpemp.gestionfp.service.ServicioAlumno;
import com.fpemp.gestionfp.service.ServicioEmpresa;
import com.fpemp.gestionfp.service.ServicioPractica;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Hafdala Mehdi Sidi
 * Controlador para la gestión de prácticas.
 * Accesible para todos los profesores.
 * Al crear una práctica se envía un email automático al alumno.
 */
@Controller
@RequestMapping("/practicas")
public class ControladorPractica {

    @Autowired
    private ServicioPractica servicioPractica;

    @Autowired
    private ServicioAlumno servicioAlumno;

    @Autowired
    private ServicioEmpresa servicioEmpresa;

    // Muestra la lista de todas las prácticas
    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("practicas", servicioPractica.obtenerTodas());
        return "practicas/lista";
    }

    // Muestra el formulario para crear una nueva práctica
    @GetMapping("/nueva")
    public String mostrarFormularioNuevo(Model modelo) {
        modelo.addAttribute("practica", new Practica());
        modelo.addAttribute("alumnos", servicioAlumno.obtenerTodos());
        modelo.addAttribute("empresas", servicioEmpresa.obtenerTodas());
        return "practicas/formulario";
    }

    // Recibe los datos del formulario y guarda la nueva práctica
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("practica") Practica practica,
                          BindingResult resultado,
                          Model modelo,
                          RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            modelo.addAttribute("alumnos", servicioAlumno.obtenerTodos());
            modelo.addAttribute("empresas", servicioEmpresa.obtenerTodas());
            return "practicas/formulario";
        }

        servicioPractica.guardar(practica);
        atributos.addFlashAttribute("mensaje",
                "Práctica creada correctamente. Se ha enviado un email al alumno.");
        return "redirect:/practicas";
    }

    // Muestra el formulario para editar una práctica existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("practica", servicioPractica.obtenerPorId(id));
        modelo.addAttribute("alumnos", servicioAlumno.obtenerTodos());
        modelo.addAttribute("empresas", servicioEmpresa.obtenerTodas());
        return "practicas/formulario";
    }

    // Recibe los datos del formulario y actualiza la práctica
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("practica") Practica practica,
                             BindingResult resultado,
                             Model modelo,
                             RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            modelo.addAttribute("alumnos", servicioAlumno.obtenerTodos());
            modelo.addAttribute("empresas", servicioEmpresa.obtenerTodas());
            return "practicas/formulario";
        }

        servicioPractica.actualizar(id, practica);
        atributos.addFlashAttribute("mensaje", "Práctica actualizada correctamente");
        return "redirect:/practicas";
    }

    // Elimina una práctica por su id
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            servicioPractica.eliminar(id);
            atributos.addFlashAttribute("mensaje", "Práctica eliminada correctamente");
        } catch (Exception e) {
            atributos.addFlashAttribute("error", "No se puede eliminar esta práctica");
        }
        return "redirect:/practicas";
    }
}

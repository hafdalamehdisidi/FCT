package com.fpemp.gestionfp.controller;

import com.fpemp.gestionfp.model.Curso;
import com.fpemp.gestionfp.service.ServicioCurso;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Hafdala Mehdi Sidi
 * Controlador para la gestión de cursos.
 * Solo accesible para profesores de la Directiva.
 */
@Controller
@RequestMapping("/cursos")
public class ControladorCurso {

    @Autowired
    private ServicioCurso servicioCurso;

    // Muestra la lista de todos los cursos
    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("cursos", servicioCurso.obtenerTodos());
        return "cursos/lista";
    }

    // Muestra el formulario para crear un nuevo curso
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model modelo) {
        modelo.addAttribute("curso", new Curso());
        return "cursos/formulario";
    }

    // Recibe los datos del formulario y guarda el nuevo curso
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("curso") Curso curso,
                          BindingResult resultado,
                          RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            return "cursos/formulario";
        }

        // Comprobamos que no exista ya un curso con ese nombre
        if (servicioCurso.existeNombre(curso.getNombre())) {
            resultado.rejectValue("nombre", "error.nombre", "Ya existe un curso con ese nombre");
            return "cursos/formulario";
        }

        servicioCurso.guardar(curso);
        atributos.addFlashAttribute("mensaje", "Curso creado correctamente");
        return "redirect:/cursos";
    }

    // Muestra el formulario para editar un curso existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("curso", servicioCurso.obtenerPorId(id));
        return "cursos/formulario";
    }

    // Recibe los datos del formulario y actualiza el curso
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("curso") Curso curso,
                             BindingResult resultado,
                             RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            return "cursos/formulario";
        }

        servicioCurso.actualizar(id, curso);
        atributos.addFlashAttribute("mensaje", "Curso actualizado correctamente");
        return "redirect:/cursos";
    }

    // Elimina un curso por su id
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            servicioCurso.eliminar(id);
            atributos.addFlashAttribute("mensaje", "Curso eliminado correctamente");
        } catch (Exception e) {
            atributos.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cursos";
    }
}

package com.fpemp.gestionfp.controller;

import com.fpemp.gestionfp.model.Alumno;
import com.fpemp.gestionfp.model.Curso;
import com.fpemp.gestionfp.service.ServicioAlumno;
import com.fpemp.gestionfp.service.ServicioCurso;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Hafdala Mehdi Sidi
 * Controlador para la gestión de alumnos.
 * Accesible para todos los profesores.
 * Incluye CRUD completo e importación desde CSV.
 * Los alumnos se muestran filtrados por curso.
 */
@Controller
@RequestMapping("/alumnos")
public class ControladorAlumno {

    @Autowired
    private ServicioAlumno servicioAlumno;

    @Autowired
    private ServicioCurso servicioCurso;

    // Muestra la lista de alumnos, filtrada por curso si se selecciona uno
    @GetMapping
    public String listar(@RequestParam(required = false) Long cursoId, Model modelo) {

        // Cargamos todos los cursos para el selector del filtro
        modelo.addAttribute("cursos", servicioCurso.obtenerTodos());

        if (cursoId != null) {
            // Si se ha seleccionado un curso, filtramos los alumnos por ese curso
            Curso curso = servicioCurso.obtenerPorId(cursoId);
            modelo.addAttribute("alumnos", servicioAlumno.obtenerPorCurso(curso));
            modelo.addAttribute("cursoSeleccionado", cursoId);
        } else {
            // Si no se ha seleccionado curso, mostramos todos los alumnos
            modelo.addAttribute("alumnos", servicioAlumno.obtenerTodos());
        }

        return "alumnos/lista";
    }

    // Muestra el formulario para crear un nuevo alumno
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model modelo) {
        modelo.addAttribute("alumno", new Alumno());
        modelo.addAttribute("cursos", servicioCurso.obtenerTodos());
        return "alumnos/formulario";
    }

    // Recibe los datos del formulario y guarda el nuevo alumno
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("alumno") Alumno alumno,
                          BindingResult resultado,
                          Model modelo,
                          RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            modelo.addAttribute("cursos", servicioCurso.obtenerTodos());
            return "alumnos/formulario";
        }

        // Comprobamos que no exista ya un alumno con ese email
        if (servicioAlumno.existeEmail(alumno.getEmail())) {
            resultado.rejectValue("email", "error.email", "Este email ya está registrado");
            modelo.addAttribute("cursos", servicioCurso.obtenerTodos());
            return "alumnos/formulario";
        }

        servicioAlumno.guardar(alumno);
        atributos.addFlashAttribute("mensaje", "Alumno creado correctamente");
        return "redirect:/alumnos";
    }

    // Muestra el formulario para editar un alumno existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("alumno", servicioAlumno.obtenerPorId(id));
        modelo.addAttribute("cursos", servicioCurso.obtenerTodos());
        return "alumnos/formulario";
    }

    // Recibe los datos del formulario y actualiza el alumno
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("alumno") Alumno alumno,
                             BindingResult resultado,
                             Model modelo,
                             RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            modelo.addAttribute("cursos", servicioCurso.obtenerTodos());
            return "alumnos/formulario";
        }

        servicioAlumno.actualizar(id, alumno);
        atributos.addFlashAttribute("mensaje", "Alumno actualizado correctamente");
        return "redirect:/alumnos";
    }

    // Elimina un alumno por su id
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            servicioAlumno.eliminar(id);
            atributos.addFlashAttribute("mensaje", "Alumno eliminado correctamente");
        } catch (Exception e) {
            atributos.addFlashAttribute("error", "No se puede eliminar este alumno");
        }
        return "redirect:/alumnos";
    }

    // Muestra el formulario de importación CSV
    @GetMapping("/importar")
    public String mostrarFormularioImportar(Model modelo) {
        modelo.addAttribute("cursos", servicioCurso.obtenerTodos());
        return "alumnos/importar";
    }

    // Recibe el fichero CSV y lo procesa
    @PostMapping("/importar")
    public String importar(@RequestParam("fichero") MultipartFile fichero,
                           @RequestParam("cursoId") Long cursoId,
                           RedirectAttributes atributos) {
        try {
            Curso curso = servicioCurso.obtenerPorId(cursoId);
            int importados = servicioAlumno.importarDesdeCSV(fichero, curso);
            atributos.addFlashAttribute("mensaje",
                    "Importación completada. Alumnos importados: " + importados);
        } catch (Exception e) {
            atributos.addFlashAttribute("error",
                    "Error al importar el fichero: " + e.getMessage());
        }
        return "redirect:/alumnos";
    }
}

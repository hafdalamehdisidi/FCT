package com.fpemp.gestionfp.controller;

import com.fpemp.gestionfp.model.Empresa;
import com.fpemp.gestionfp.service.ServicioEmpresa;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Hafdala Mehdi Sidi
 * Controlador para la gestión de empresas.
 * Accesible para todos los profesores, incluida la Directiva.
 */
@Controller
@RequestMapping("/empresas")
public class ControladorEmpresa {

    @Autowired
    private ServicioEmpresa servicioEmpresa;

    // Muestra la lista de todas las empresas
    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("empresas", servicioEmpresa.obtenerTodas());
        return "empresas/lista";
    }

    // Muestra el formulario para crear una nueva empresa
    @GetMapping("/nueva")
    public String mostrarFormularioNuevo(Model modelo) {
        modelo.addAttribute("empresa", new Empresa());
        return "empresas/formulario";
    }

    // Recibe los datos del formulario y guarda la nueva empresa
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("empresa") Empresa empresa,
                          BindingResult resultado,
                          RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            return "empresas/formulario";
        }

        // Comprobamos que no exista ya una empresa con ese nombre
        if (servicioEmpresa.existeNombre(empresa.getNombre())) {
            resultado.rejectValue("nombre", "error.nombre", "Ya existe una empresa con ese nombre");
            return "empresas/formulario";
        }

        servicioEmpresa.guardar(empresa);
        atributos.addFlashAttribute("mensaje", "Empresa creada correctamente");
        return "redirect:/empresas";
    }

    // Muestra el formulario para editar una empresa existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("empresa", servicioEmpresa.obtenerPorId(id));
        return "empresas/formulario";
    }

    // Recibe los datos del formulario y actualiza la empresa
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("empresa") Empresa empresa,
                             BindingResult resultado,
                             RedirectAttributes atributos) {

        if (resultado.hasErrors()) {
            return "empresas/formulario";
        }

        servicioEmpresa.actualizar(id, empresa);
        atributos.addFlashAttribute("mensaje", "Empresa actualizada correctamente");
        return "redirect:/empresas";
    }

    // Elimina una empresa por su id
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            servicioEmpresa.eliminar(id);
            atributos.addFlashAttribute("mensaje", "Empresa eliminada correctamente");
        } catch (Exception e) {
            atributos.addFlashAttribute("error", "No se puede eliminar esta empresa");
        }
        return "redirect:/empresas";
    }
}

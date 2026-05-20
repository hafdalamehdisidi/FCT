package com.fpemp.gestionfp.controller;

import com.fpemp.gestionfp.repository.PracticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hafdala Mehdi Sidi
 * Controlador para la página de estadísticas.
 * Solo accesible para profesores de la Directiva.
 * Muestra el número de alumnos por empresa y por curso
 * tanto en tabla como en gráficas.
 */
@Controller
public class ControladorEstadisticas {

    @Autowired
    private PracticaRepository repositorioPractica;

    // Solo la Directiva puede ver las estadísticas (configurado en SecurityConfig)
    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(Model modelo) {

        // Obtenemos los datos agrupados por empresa desde el repositorio
        List<Object[]> datosPorEmpresa = repositorioPractica.countAlumnosPorEmpresa();

        // Guardamos los datos en un mapa para pasarlos a la vista
        // La clave es el nombre de la empresa y el valor es el número de alumnos
        Map<String, Long> alumnosPorEmpresa = new LinkedHashMap<>();
        for (Object[] fila : datosPorEmpresa) {
            String nombreEmpresa = ((com.fpemp.gestionfp.model.Empresa) fila[0]).getNombre();
            Long cantidad = (Long) fila[1];
            alumnosPorEmpresa.put(nombreEmpresa, cantidad);
        }

        // Obtenemos los datos agrupados por curso desde el repositorio
        List<Object[]> datosPorCurso = repositorioPractica.countAlumnosPorCurso();

        Map<String, Long> alumnosPorCurso = new LinkedHashMap<>();
        for (Object[] fila : datosPorCurso) {
            String nombreCurso = ((com.fpemp.gestionfp.model.Curso) fila[0]).getNombre();
            Long cantidad = (Long) fila[1];
            alumnosPorCurso.put(nombreCurso, cantidad);
        }

        // Pasamos los datos a la vista
        modelo.addAttribute("alumnosPorEmpresa", alumnosPorEmpresa);
        modelo.addAttribute("alumnosPorCurso", alumnosPorCurso);

        return "estadisticas";
    }
}

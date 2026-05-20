package com.fpemp.gestionfp.controller;

import com.fpemp.gestionfp.model.Alumno;
import com.fpemp.gestionfp.service.ServicioAlumno;
import com.fpemp.gestionfp.service.ServicioCurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Hafdala Mehdi Sidi
 * Controlador REST para la gestión de alumnos.
 * Permite consultar, crear y actualizar alumnos desde una app móvil
 * o cualquier cliente externo que consuma la API.
 * Las respuestas se devuelven en formato JSON automáticamente.
 */
// @RestController indica que este controlador devuelve JSON, no vistas HTML
@RestController
@RequestMapping("/api/alumnos")
public class ControladorRestAlumno {

    @Autowired
    private ServicioAlumno servicioAlumno;

    @Autowired
    private ServicioCurso servicioCurso;

    /**
     * GET /api/alumnos
     * Devuelve la lista de todos los alumnos en formato JSON.
     */
    @GetMapping
    public ResponseEntity<List<Alumno>> obtenerTodos() {
        List<Alumno> alumnos = servicioAlumno.obtenerTodos();
        return ResponseEntity.ok(alumnos);
    }

    /**
     * GET /api/alumnos/{id}
     * Devuelve un alumno concreto por su id en formato JSON.
     * Si no existe devuelve un error 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> obtenerPorId(@PathVariable Long id) {
        try {
            Alumno alumno = servicioAlumno.obtenerPorId(id);
            return ResponseEntity.ok(alumno);
        } catch (Exception e) {
            // 404 Not Found si el alumno no existe
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/alumnos/curso/{cursoId}
     * Devuelve todos los alumnos de un curso concreto en formato JSON.
     */
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Alumno>> obtenerPorCurso(@PathVariable Long cursoId) {
        try {
            List<Alumno> alumnos = servicioAlumno.obtenerPorCurso(
                    servicioCurso.obtenerPorId(cursoId));
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/alumnos
     * Crea un nuevo alumno recibiendo los datos en formato JSON.
     * Devuelve el alumno creado con código 201 Created.
     */
    @PostMapping
    public ResponseEntity<Alumno> crear(@RequestBody Alumno alumno) {
        try {
            // Comprobamos que el email no esté ya registrado
            if (servicioAlumno.existeEmail(alumno.getEmail())) {
                // 409 Conflict si el email ya existe
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            servicioAlumno.guardar(alumno);
            return ResponseEntity.status(HttpStatus.CREATED).body(alumno);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/alumnos/{id}
     * Actualiza un alumno existente recibiendo los datos en formato JSON.
     * Devuelve el alumno actualizado o 404 si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> actualizar(@PathVariable Long id,
                                             @RequestBody Alumno alumno) {
        try {
            servicioAlumno.actualizar(id, alumno);
            return ResponseEntity.ok(servicioAlumno.obtenerPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

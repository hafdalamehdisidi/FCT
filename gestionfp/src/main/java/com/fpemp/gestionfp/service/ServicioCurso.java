package com.fpemp.gestionfp.service;

import com.fpemp.gestionfp.model.Curso;
import com.fpemp.gestionfp.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Hafdala Mehdi Sidi
 * Servicio para la gestión de cursos.
 * Contiene la lógica de negocio para crear, editar, eliminar y listar cursos.
 */

@Service
public class ServicioCurso {

    @Autowired
    private CursoRepository repositorioCurso;

    // Devuelve todos los cursos de la base de datos
    public List<Curso> obtenerTodos() {
        return repositorioCurso.findAll();
    }

    // Busca un curso por su id, lanza excepción si no existe
    public Curso obtenerPorId(Long id) {
        return repositorioCurso.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + id));
    }

    // Guarda un curso nuevo en la base de datos
    public void guardar(Curso curso) {
        repositorioCurso.save(curso);
    }

    // Actualiza el nombre de un curso existente
    public void actualizar(Long id, Curso cursoActualizado) {
        Curso cursoExistente = obtenerPorId(id);
        cursoExistente.setNombre(cursoActualizado.getNombre());
        repositorioCurso.save(cursoExistente);
    }

    // Elimina un curso por su id
    // Solo se puede eliminar si no tiene alumnos asociados
    public void eliminar(Long id) {
        Curso curso = obtenerPorId(id);
        if (curso.getAlumnos() != null && !curso.getAlumnos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar un curso con alumnos matriculados");
        }
        repositorioCurso.deleteById(id);
    }

    // Comprueba si ya existe un curso con ese nombre
    public boolean existeNombre(String nombre) {
        return repositorioCurso.existsByNombre(nombre);
    }
}
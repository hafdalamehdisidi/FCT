package com.fpemp.gestionfp.service;

import com.fpemp.gestionfp.model.Empresa;
import com.fpemp.gestionfp.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Hafdala Mehdi Sidi
 * Servicio para la gestión de empresas.
 * Contiene la lógica de negocio para crear, editar, eliminar y listar empresas.
 * Accesible para todos los profesores, incluida la Directiva.
 */
@Service
public class ServicioEmpresa {

    @Autowired
    private EmpresaRepository repositorioEmpresa;

    // Devuelve todas las empresas de la base de datos
    public List<Empresa> obtenerTodas() {
        return repositorioEmpresa.findAll();
    }

    // Busca una empresa por su id, lanza excepción si no existe
    public Empresa obtenerPorId(Long id) {
        return repositorioEmpresa.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + id));
    }

    // Guarda una empresa nueva en la base de datos
    public void guardar(Empresa empresa) {
        repositorioEmpresa.save(empresa);
    }

    // Actualiza los datos de una empresa existente
    public void actualizar(Long id, Empresa empresaActualizada) {
        Empresa empresaExistente = obtenerPorId(id);
        empresaExistente.setNombre(empresaActualizada.getNombre());
        empresaExistente.setDescripcion(empresaActualizada.getDescripcion());
        empresaExistente.setNombreTutor(empresaActualizada.getNombreTutor());
        empresaExistente.setEmailTutor(empresaActualizada.getEmailTutor());
        repositorioEmpresa.save(empresaExistente);
    }

    // Elimina una empresa por su id
    // Solo se puede eliminar si no tiene prácticas asociadas
    public void eliminar(Long id) {
        repositorioEmpresa.deleteById(id);
    }

    // Comprueba si ya existe una empresa con ese nombre
    public boolean existeNombre(String nombre) {
        return repositorioEmpresa.existsByNombre(nombre);
    }
}


package com.fpemp.gestionfp.service;

import com.fpemp.gestionfp.model.Alumno;
import com.fpemp.gestionfp.model.Curso;
import com.fpemp.gestionfp.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Hafdala Mehdi Sidi
 * Servicio para la gestión de alumnos.
 * Incluye el CRUD básico y la importación de alumnos desde un fichero CSV.
 * Los alumnos se consultan filtrados por curso.
 */
@Service
public class ServicioAlumno {

    @Autowired
    private AlumnoRepository repositorioAlumno;

    // Devuelve todos los alumnos de la base de datos
    public List<Alumno> obtenerTodos() {
        return repositorioAlumno.findAll();
    }

    // Devuelve los alumnos filtrados por curso
    public List<Alumno> obtenerPorCurso(Curso curso) {
        return repositorioAlumno.findByCurso(curso);
    }

    // Busca un alumno por su id, lanza excepción si no existe
    public Alumno obtenerPorId(Long id) {
        return repositorioAlumno.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + id));
    }

    // Guarda un alumno nuevo en la base de datos
    public void guardar(Alumno alumno) {
        repositorioAlumno.save(alumno);
    }

    // Actualiza los datos de un alumno existente
    public void actualizar(Long id, Alumno alumnoActualizado) {
        Alumno alumnoExistente = obtenerPorId(id);
        alumnoExistente.setNombre(alumnoActualizado.getNombre());
        alumnoExistente.setApellidos(alumnoActualizado.getApellidos());
        alumnoExistente.setEmail(alumnoActualizado.getEmail());
        alumnoExistente.setFechaNacimiento(alumnoActualizado.getFechaNacimiento());
        alumnoExistente.setCurso(alumnoActualizado.getCurso());
        repositorioAlumno.save(alumnoExistente);
    }

    // Elimina un alumno por su id
    public void eliminar(Long id) {
        repositorioAlumno.deleteById(id);
    }

    // Comprueba si ya existe un alumno con ese email
    public boolean existeEmail(String email) {
        return repositorioAlumno.existsByEmail(email);
    }

    /**
     * Importa alumnos desde un fichero CSV.
     * El formato esperado de cada línea es:
     * nombre,apellidos,email,fechaNacimiento (dd/MM/yyyy)
     * El curso se pasa como parámetro porque todos los alumnos
     * del CSV pertenecen al mismo curso.
     */
    public int importarDesdeCSV(MultipartFile fichero, Curso curso) throws Exception {

        int alumnosImportados = 0;

        // Abrimos el fichero para leerlo línea a línea
        BufferedReader lector = new BufferedReader(
                new InputStreamReader(fichero.getInputStream(), "UTF-8"));

        String linea;
        // Saltamos la primera línea si es la cabecera del CSV
        boolean esCabecera = true;

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while ((linea = lector.readLine()) != null) {

            // Saltamos la línea de cabecera
            if (esCabecera) {
                esCabecera = false;
                continue;
            }

            // Separamos los campos por coma
            String[] campos = linea.split(",");

            if (campos.length < 4) continue;

            String email = campos[2].trim();

            // Si el alumno ya existe en la base de datos lo saltamos
            if (existeEmail(email)) continue;

            Alumno alumno = new Alumno();
            alumno.setNombre(campos[0].trim());
            alumno.setApellidos(campos[1].trim());
            alumno.setEmail(email);
            alumno.setFechaNacimiento(LocalDate.parse(campos[3].trim(), formato));
            alumno.setCurso(curso);

            repositorioAlumno.save(alumno);
            alumnosImportados++;
        }

        lector.close();
        return alumnosImportados;
    }
}

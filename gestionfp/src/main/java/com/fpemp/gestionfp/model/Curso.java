package com.fpemp.gestionfp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Hafdala Mehdi Sidi
 */

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El nombre del curso debe ser único (ej: "1º DAW", "2º DAW")
    @NotBlank(message = "El nombre es obligatorio")
    @Column(unique = true)
    private String nombre;

    // Un curso tiene muchos alumnos matriculados
    @OneToMany(mappedBy = "curso")
    private List<Alumno> alumnos;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Alumno> getAlumnos() { return alumnos; }
    public void setAlumnos(List<Alumno> alumnos) { this.alumnos = alumnos; }
}
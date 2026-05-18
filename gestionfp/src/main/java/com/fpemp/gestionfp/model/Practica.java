package com.fpemp.gestionfp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
/**
 * @author Hafdala Mehdi Sidi
 */

@Entity
@Table(name = "practicas")
public class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Alumno que realiza la práctica
    @ManyToOne
    @JoinColumn(name = "alumno_id")
    @NotNull(message = "El alumno es obligatorio")
    private Alumno alumno;

    // Empresa donde realiza la práctica
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    @NotNull(message = "La empresa es obligatoria")
    private Empresa empresa;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    // Campo de texto libre para que el profesor añada comentarios
    @Column(columnDefinition = "TEXT")
    private String comentarios;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
}

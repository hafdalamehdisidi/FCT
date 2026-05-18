package com.fpemp.gestionfp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Hafdala Mehdi Sidi
 */
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    // Descripción de la empresa
    private String descripcion;

    // Nombre completo del tutor laboral en la empresa
    @NotBlank(message = "El nombre del tutor es obligatorio")
    private String nombreTutor;

    @Email(message = "El email del tutor no es válido")
    @NotBlank(message = "El email del tutor es obligatorio")
    private String emailTutor;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getNombreTutor() { return nombreTutor; }
    public void setNombreTutor(String nombreTutor) { this.nombreTutor = nombreTutor; }

    public String getEmailTutor() { return emailTutor; }
    public void setEmailTutor(String emailTutor) { this.emailTutor = emailTutor; }
}

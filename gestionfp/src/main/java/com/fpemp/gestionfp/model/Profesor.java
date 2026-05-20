package com.fpemp.gestionfp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Hafdala Mehdi Sidi
 */

@Entity
@Table(name = "profesores")
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del profesor
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    // Apellidos del profesor
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @Email(message = "El email no es válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(unique = true)
    private String email;

    // Contraseña encriptada (nunca se guarda en texto plano)
    private String password;

    // true = pertenece a la Directiva, false = profesor normal
    private boolean directiva;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isDirectiva() { return directiva; }
    public void setDirectiva(boolean directiva) { this.directiva = directiva; }
}


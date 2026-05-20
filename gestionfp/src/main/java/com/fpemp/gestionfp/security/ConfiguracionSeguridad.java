package com.fpemp.gestionfp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Hafdala Mehdi Sidi
 * Configuración principal de Spring Security.
 * Aquí definimos qué páginas son públicas, cuáles requieren login,
 * y cuáles son solo para la Directiva.
 */
@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    @Autowired
    private ServicioUsuarioImpl servicioDatosUsuario;

    // BCrypt es el algoritmo que usamos para encriptar contraseñas
    // Nunca guardamos contraseñas en texto plano en la base de datos
    @Bean
    public PasswordEncoder codificadorPassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain cadenaFiltros(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(autorizacion -> autorizacion
                        // Páginas públicas: login y recursos estáticos (CSS, JS)
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                        // Solo la Directiva puede gestionar profesores y cursos
                        .requestMatchers("/profesores/**", "/cursos/**").hasRole("DIRECTIVA")
                        // El resto de páginas requieren estar autenticado
                        .anyRequest().authenticated()
                )
                .formLogin(formulario -> formulario
                        // Página de login personalizada
                        .loginPage("/login")
                        // Después del login exitoso, ir al inicio
                        .defaultSuccessUrl("/inicio", true)
                        .permitAll()
                )
                .logout(cerrarSesion -> cerrarSesion
                        // Al hacer logout, volvemos al login
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager gestorAutenticacion(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder constructor =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        // Usamos nuestra implementación y BCrypt para verificar contraseñas
        constructor.userDetailsService(servicioDatosUsuario)
                .passwordEncoder(codificadorPassword());
        return constructor.build();
    }
}

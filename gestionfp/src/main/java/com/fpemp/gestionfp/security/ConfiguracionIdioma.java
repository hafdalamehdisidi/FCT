package com.fpemp.gestionfp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import java.util.Locale;

/**
 * @author Hafdala Mehdi Sidi
 * Configuración de internacionalización.
 * Permite cambiar el idioma de la aplicación mediante un parámetro
 * en la URL: ?lang=es para español, ?lang=en para inglés.
 */
@Configuration
public class ConfiguracionIdioma implements WebMvcConfigurer {

    // Guardamos el idioma en la sesión del usuario
    // Por defecto el idioma es español
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale("es"));
        return resolver;
    }

    // Interceptor que detecta el parámetro ?lang=xx en la URL
    // y cambia el idioma de la sesión
    @Bean
    public LocaleChangeInterceptor interceptorCambioIdioma() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    // Registramos el interceptor para que funcione en todas las peticiones
    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(interceptorCambioIdioma());
    }
}

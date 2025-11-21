package br.feevale.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .toList();

        final var corsConfiguration = new CorsConfiguration();
        // corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        // corsConfiguration.setAllowedOrigins(origins);
        // corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // corsConfiguration.setAllowedHeaders(List.of("*"));
        // corsConfiguration.setAllowCredentials(true);

        // liberando tudo
        corsConfiguration.setAllowedOriginPatterns(List.of("*")); // permite qualquer origem
        corsConfiguration.setAllowedMethods(List.of("*"));        // permite todos os métodos (GET, POST, PUT, etc)
        corsConfiguration.setAllowedHeaders(List.of("*"));        // permite todos os headers
        corsConfiguration.setAllowCredentials(true);               // permite envio de cookies/autenticação se necessário

        final var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }
}

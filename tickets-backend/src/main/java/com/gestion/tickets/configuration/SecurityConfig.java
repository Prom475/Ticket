package com.gestion.tickets.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    // Définition des utilisateurs en mémoire
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        // Utilisateur avec le rôle ADMIN
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin")) // Encode le mot de passe
                .roles("TECHNICIEN")
                .build();

        // Utilisateur avec le rôle USER
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user")) // Encode le mot de passe
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    // Configuration de la sécurité HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactivation de la protection CSRF (utile pour les APIs REST)
                .csrf(csrf -> csrf.disable())

                // Configuration CORS
                .cors(withDefaults())

                // Définition des règles d'autorisation
                .authorizeHttpRequests(authz -> authz
                        // Accès réservé aux admins pour certains endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/utilisateurs/**").hasRole("ADMIN")

                        // Accès public pour les routes de l'authentification et des utilisateurs
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/utilisateurs/**","/api/tickets/**","/api/tickets/create","/api/departements/**").permitAll()

                        // Accès réservé aux utilisateurs authentifiés pour les tickets
                        .requestMatchers("/api/tickets/**").hasRole("USER")

                        // Permet toutes les autres requêtes (par défaut, permet tout)
                        .anyRequest().authenticated()
                )

                // Activation de la connexion HTTP Basic et formulaire de connexion
                .httpBasic(withDefaults())
                .formLogin(withDefaults())

                // JWT Authentication Filter
                .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuration CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Autoriser Angular en local
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Autoriser les cookies si nécessaire

        // Appliquer CORS à toutes les requêtes API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // Bean pour l'encodage des mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

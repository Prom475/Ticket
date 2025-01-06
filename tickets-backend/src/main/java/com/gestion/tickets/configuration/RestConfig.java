package com.gestion.tickets.configuration;

import com.gestion.tickets.model.Utilisateur;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Utilisateur.class);  // Expose les IDs dans les réponses
    }
}


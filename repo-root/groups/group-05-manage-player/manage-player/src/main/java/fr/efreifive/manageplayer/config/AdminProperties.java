package fr.efreifive.manageplayer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.admin")
public record AdminProperties(boolean enabled) {
    public AdminProperties() {
        this(false);
    }
}

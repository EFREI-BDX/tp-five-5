package com.jad.efreifive.managefield.security;

import com.jad.efreifive.managefield.exception.UnauthorizedException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ConfigurationProperties(prefix = "managefield.security")
public class ApiKeyProperties {

    private String apiKey;

    public boolean matches(String candidate) {
        return StringUtils.hasText(apiKey) && apiKey.equals(candidate);
    }

    public void validate() {
        if (!StringUtils.hasText(apiKey)) {
            throw new UnauthorizedException("API key must be configured.");
        }
    }
}

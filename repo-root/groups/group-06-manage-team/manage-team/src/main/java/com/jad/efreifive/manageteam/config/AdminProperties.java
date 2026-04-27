package com.jad.efreifive.manageteam.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "admin")
public class AdminProperties {
    private String playersSourceUrl;

    public String getPlayersSourceUrl() {
        return playersSourceUrl;
    }

    public void setPlayersSourceUrl(String playersSourceUrl) {
        this.playersSourceUrl = playersSourceUrl;
    }
}


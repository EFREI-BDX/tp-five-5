package com.jad.efreifive.manageteam.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "team.outbound")
public class TeamOutboundProperties {
    @Getter
    @Setter
    private Map<String, List<String>> notifyUrls;
}
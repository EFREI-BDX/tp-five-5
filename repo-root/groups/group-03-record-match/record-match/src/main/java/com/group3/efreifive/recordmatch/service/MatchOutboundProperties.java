package com.group3.efreifive.recordmatch.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "match.outbound")
public class MatchOutboundProperties {
    @Getter
    @Setter
    private Map<String, List<String>> notifyUrls;
}

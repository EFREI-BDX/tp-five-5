package org.efrei.five.apimanagematch.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "match.outbound")
public class MatchOutboundProperties {

    private Map<String, List<String>> notifyUrls = Map.of();

    public Map<String, List<String>> getNotifyUrls() {
        return notifyUrls;
    }

    public void setNotifyUrls(Map<String, List<String>> notifyUrls) {
        this.notifyUrls = notifyUrls;
    }
}
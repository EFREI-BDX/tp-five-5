package com.group3.efreifive.recordmatch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
 
/**
 * Configuration de Jackson pour la sérialisation/désérialisation JSON
 */
@Configuration
public class JacksonConfig {

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final Class<?> jt = Class.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");
            final com.fasterxml.jackson.databind.Module module = (com.fasterxml.jackson.databind.Module) jt.getDeclaredConstructor().newInstance();
            mapper.registerModule(module);
        } catch (final ClassNotFoundException ignored) {
            // jackson-datatype-jsr310 not available on classpath; fall back
        } catch (final Exception ignored) {
            // ignore other reflection errors
        }
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}

package fr.efrei.managefield.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Registers HTTP infrastructure for the application.
 */
@Configuration
class WebConfig(
    private val apiKeyInterceptor: ApiKeyInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(apiKeyInterceptor)
    }
}

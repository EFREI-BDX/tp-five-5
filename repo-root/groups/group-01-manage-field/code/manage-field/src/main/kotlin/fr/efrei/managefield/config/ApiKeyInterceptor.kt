package fr.efrei.managefield.config

import fr.efrei.managefield.service.exception.ApplicationUnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.Objects

/**
 * Verifies the `X-API-KEY` header required by the published API contract.
 */
@Component
class ApiKeyInterceptor(
    @Value($$"${security.api-key}") private val configuredApiKey: String
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.requestURI == "/health") {
            return true
        }

        val expectedApiKey = Objects.requireNonNull(configuredApiKey, "security.api-key must not be null")
        val providedApiKey = request.getHeader(API_KEY_HEADER)
        if (expectedApiKey.isBlank() || providedApiKey != expectedApiKey) {
            throw ApplicationUnauthorizedException("invalid API key")
        }

        return true
    }

    companion object {
        /** Header carrying the client API key. */
        const val API_KEY_HEADER = "X-API-KEY"
    }
}

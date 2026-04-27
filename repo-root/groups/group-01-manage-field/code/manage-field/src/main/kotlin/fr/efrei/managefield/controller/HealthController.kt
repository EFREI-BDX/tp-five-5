package fr.efrei.managefield.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Exposes the service health endpoint declared in the service contract.
 */
@RestController
class HealthController {
    @GetMapping("/health")
    fun health(): Map<String, String> {
        return mapOf("status" to "UP")
    }
}

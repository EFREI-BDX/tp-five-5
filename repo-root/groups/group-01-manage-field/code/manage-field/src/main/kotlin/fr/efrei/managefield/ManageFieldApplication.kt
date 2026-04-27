package fr.efrei.managefield

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Entry point for the manage-field Spring Boot application.
 */
@SpringBootApplication
class ManageFieldApplication

/**
 * Starts the Spring Boot application with the provided command-line arguments.
 *
 * @param args startup arguments passed to the application
 */
fun main(args: Array<String>) {
    runApplication<ManageFieldApplication>(*args)
}

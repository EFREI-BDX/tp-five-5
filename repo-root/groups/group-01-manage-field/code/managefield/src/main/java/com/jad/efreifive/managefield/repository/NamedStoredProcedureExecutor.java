package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.exception.BadRequestException;
import com.jad.efreifive.managefield.exception.ConflictException;
import com.jad.efreifive.managefield.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

@Component
public class NamedStoredProcedureExecutor {

    private static final Pattern MARIADB_CONNECTION_PREFIX = Pattern.compile("^\\(conn=\\d+\\)\\s*");

    @PersistenceContext
    private EntityManager entityManager;

    <T> T queryForSingleResult(
        String procedureName,
        Consumer<StoredProcedureQuery> parameterBinder,
        Function<Object, T> resultMapper
    ) {
        try {
            StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery(procedureName);
            parameterBinder.accept(query);

            List<?> results = query.getResultList();
            if (results.isEmpty()) {
                throw new IllegalStateException("Stored procedure did not return a result.");
            }
            return resultMapper.apply(results.getFirst());
        } catch (PersistenceException exception) {
            throw translate(exception);
        }
    }

    private RuntimeException translate(RuntimeException exception) {
        String message = extractMessage(exception);
        String normalizedMessage = message.toLowerCase(Locale.ROOT);

        if (normalizedMessage.contains("not found")) {
            return new NotFoundException(message);
        }
        if (normalizedMessage.contains("already overlaps") || normalizedMessage.contains("not active")) {
            return new ConflictException(message);
        }
        if (normalizedMessage.contains("required") || normalizedMessage.contains("must ")) {
            return new BadRequestException(message);
        }

        return exception;
    }

    private String extractMessage(Throwable throwable) {
        Throwable current = throwable;
        String message = "Database procedure call failed.";

        while (current != null) {
            if (current.getMessage() != null && !current.getMessage().isBlank()) {
                message = cleanMessage(current.getMessage());
            }
            current = current.getCause();
        }

        return message;
    }

    private String cleanMessage(String message) {
        return MARIADB_CONNECTION_PREFIX.matcher(message.trim())
            .replaceFirst("");
    }
}

package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NamedStoredProcedureExecutorTest {

    private final EntityManager entityManager = mock(EntityManager.class);
    private final NamedStoredProcedureExecutor executor = new NamedStoredProcedureExecutor();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(executor, "entityManager", entityManager);
    }

    @Test
    void shouldStripMariaDbConnectionPrefixFromProcedureErrors() {
        when(entityManager.createNamedStoredProcedureQuery("sp_update_field_status"))
            .thenThrow(new PersistenceException(new SQLException("(conn=372) Field not found.")));

        assertThatThrownBy(() -> executor.queryForSingleResult(
            "sp_update_field_status",
            query -> {
            },
            result -> result
        ))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Field not found.");
    }
}

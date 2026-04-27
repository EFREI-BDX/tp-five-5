package fr.efrei.managefield;

import fr.efrei.managefield.entity.FieldEntity;
import fr.efrei.managefield.entity.ReservationEntity;
import fr.efrei.managefield.repository.FieldRepository;
import fr.efrei.managefield.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.query.Procedure;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies stored procedures are invoked from repositories, not entities.
 */
class RepositoryProcedureAnnotationTest {
    @Test
    void repositoriesExposeProcedureMethods() throws NoSuchMethodException {
        assertThat(FieldRepository.class.getMethod("createField", String.class, String.class, String.class))
            .matches(method -> method.isAnnotationPresent(Procedure.class));
        assertThat(FieldRepository.class.getMethod("changeFieldStatus", String.class, String.class))
            .matches(method -> method.isAnnotationPresent(Procedure.class));
        assertThat(ReservationRepository.class.getMethod(
            "changeReservationStatus",
            String.class,
            String.class,
            String.class
        )).matches(method -> method.isAnnotationPresent(Procedure.class));
    }

    @Test
    void entitiesDoNotExposeProcedureMethods() {
        assertThat(hasProcedureAnnotation(FieldEntity.class)).isFalse();
        assertThat(hasProcedureAnnotation(ReservationEntity.class)).isFalse();
    }

    private boolean hasProcedureAnnotation(Class<?> type) {
        for (Method method : type.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Procedure.class)) {
                return true;
            }
        }
        return false;
    }
}

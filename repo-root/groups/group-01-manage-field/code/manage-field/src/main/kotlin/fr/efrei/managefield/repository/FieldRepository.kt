package fr.efrei.managefield.repository

import fr.efrei.managefield.entity.FieldEntity
import fr.efrei.managefield.repository.procedural.CreateFieldProcedureResult
import fr.efrei.managefield.repository.procedural.SimpleProcedureResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * Repository exposing detailed field reads through a view and writes through
 * stored procedures. No procedure is invoked from an entity.
 */
@Repository
interface FieldRepository : JpaRepository<FieldEntity, String> {
    /**
     * Calls the `app_create_field` stored procedure.
     */
    @Procedure(procedureName = "app_create_field")
    fun createField(
        @Param("p_field_id") fieldId: String?,
        @Param("p_name") name: String,
        @Param("p_status_id") statusId: String
    ): CreateFieldProcedureResult

    /**
     * Calls the `app_change_field_status` stored procedure.
     */
    @Procedure(procedureName = "app_change_field_status")
    fun changeFieldStatus(
        @Param("p_field_id") fieldId: String,
        @Param("p_status_id") statusId: String
    ): SimpleProcedureResult
}

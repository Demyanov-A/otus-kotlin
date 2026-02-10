package ru.demyanovaf.kotlin.taskManager.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus

fun Table.taskTypeEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.TASK_STATUS_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.TASK_STATUS_NEW -> MgrStatus.NEW
            SqlFields.TASK_STATUS_TO_DO -> MgrStatus.TO_DO
            SqlFields.TASK_STATUS_IN_PROGRESS -> MgrStatus.IN_PROGRESS
            SqlFields.TASK_STATUS_DONE -> MgrStatus.DONE
            SqlFields.TASK_STATUS_HOLD -> MgrStatus.HOLD
            SqlFields.TASK_STATUS_CANCELED -> MgrStatus.CANCELED
            else -> MgrStatus.NONE
        }
    },
    toDb = { value ->
        when (value) {
            MgrStatus.NEW -> PgTaskStatusNew
            MgrStatus.TO_DO -> PgTaskStatusToDo
            MgrStatus.IN_PROGRESS -> PgTaskStatusInProgress
            MgrStatus.DONE -> PgTaskStatusDone
            MgrStatus.HOLD -> PgTaskStatusHold
            MgrStatus.CANCELED -> PgTaskStatusCanceled
            MgrStatus.NONE -> throw Exception("Wrong value of Task Status. NONE is unsupported")
        }
    }
)

sealed class PgTaskStatusValue(enVal: String): PGobject() {
    init {
        type = SqlFields.TASK_STATUS_TYPE
        value = enVal
    }
}

object PgTaskStatusNew: PgTaskStatusValue(SqlFields.TASK_STATUS_NEW) {
    private fun readResolve(): Any = PgTaskStatusNew
}

object PgTaskStatusToDo: PgTaskStatusValue(SqlFields.TASK_STATUS_TO_DO) {
    private fun readResolve(): Any = PgTaskStatusToDo
}

object PgTaskStatusInProgress: PgTaskStatusValue(SqlFields.TASK_STATUS_IN_PROGRESS) {
    private fun readResolve(): Any = PgTaskStatusInProgress
}

object PgTaskStatusDone: PgTaskStatusValue(SqlFields.TASK_STATUS_DONE) {
    private fun readResolve(): Any = PgTaskStatusDone
}

object PgTaskStatusHold: PgTaskStatusValue(SqlFields.TASK_STATUS_HOLD) {
    private fun readResolve(): Any = PgTaskStatusHold
}

object PgTaskStatusCanceled: PgTaskStatusValue(SqlFields.TASK_STATUS_CANCELED) {
    private fun readResolve(): Any = PgTaskStatusCanceled
}
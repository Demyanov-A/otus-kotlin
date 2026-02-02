package ru.demyanovaf.kotlin.taskManager.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory

fun Table.categoryEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.CATEGORY_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.CATEGORY_LOW -> MgrCategory.LOW
            SqlFields.CATEGORY_MEDIUM -> MgrCategory.MEDIUM
            SqlFields.CATEGORY_HI -> MgrCategory.HI
            SqlFields.CATEGORY_PERSONAL -> MgrCategory.PERSONAL
            else -> MgrCategory.NONE
        }
    },
    toDb = { value ->
        when (value) {
            MgrCategory.LOW -> PgCategoryLow
            MgrCategory.MEDIUM -> PgCategoryMedium
            MgrCategory.HI -> PgCategoryHi
            MgrCategory.PERSONAL -> PgCategoryPersonal
            MgrCategory.NONE -> throw Exception("Wrong value of Category. NONE is unsupported")
        }
    }
)

sealed class PgCategoryValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.CATEGORY_TYPE
        value = eValue
    }
}

object PgCategoryLow: PgCategoryValue(SqlFields.CATEGORY_LOW) {
    private fun readResolve(): Any = PgCategoryLow
}

object PgCategoryMedium: PgCategoryValue(SqlFields.CATEGORY_MEDIUM) {
    private fun readResolve(): Any = PgCategoryMedium
}

object PgCategoryHi: PgCategoryValue(SqlFields.CATEGORY_HI) {
    private fun readResolve(): Any = PgCategoryHi
}

object PgCategoryPersonal: PgCategoryValue(SqlFields.CATEGORY_PERSONAL) {
    private fun readResolve(): Any = PgCategoryPersonal
}

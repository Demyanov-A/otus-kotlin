package ru.demyanovaf.kotlin.taskManager.common.repo

import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: MgrTaskId) = DbTaskResponseErr(
    MgrError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbTaskResponseErr(
    MgrError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

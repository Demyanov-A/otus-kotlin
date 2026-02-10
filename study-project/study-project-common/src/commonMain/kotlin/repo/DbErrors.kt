package ru.demyanovaf.kotlin.taskManager.common.repo

import ru.demyanovaf.kotlin.taskManager.common.helpers.errorSystem
import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.repo.exceptions.RepoConcurrencyException
import ru.demyanovaf.kotlin.taskManager.common.repo.exceptions.RepoException

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

fun errorRepoConcurrency(
    oldTask: MgrTask,
    expectedLock: MgrTaskLock,
    exception: Exception = RepoConcurrencyException(
        id = oldTask.id,
        expectedLock = expectedLock,
        actualLock = oldTask.lock,
    ),
) = DbTaskResponseErrWithData(
    task = oldTask,
    err = MgrError(
        code = "${ERROR_GROUP_REPO}-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldTask.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: MgrTaskId) = DbTaskResponseErr(
    MgrError(
        code = "${ERROR_GROUP_REPO}-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Task ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbTaskResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
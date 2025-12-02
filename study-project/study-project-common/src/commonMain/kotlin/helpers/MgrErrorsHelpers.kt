package ru.demyanovaf.kotlin.taskManager.common.helpers

import ru.demyanovaf.kotlin.taskManager.common.models.MgrError

fun Throwable.asMgrError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MgrError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

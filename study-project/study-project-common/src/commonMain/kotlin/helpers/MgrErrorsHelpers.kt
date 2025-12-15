package ru.demyanovaf.kotlin.taskManager.common.helpers

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState

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

inline fun MgrContext.addError(vararg error: MgrError) = errors.addAll(error)

inline fun MgrContext.fail(error: MgrError) {
    addError(error)
    state = MgrState.FAILING
}
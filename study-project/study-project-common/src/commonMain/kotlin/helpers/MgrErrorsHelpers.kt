package ru.demyanovaf.kotlin.taskManager.common.helpers

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.logging.common.LogLevel

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

inline fun MgrContext.addError(error: MgrError) = errors.add(error)
inline fun MgrContext.addErrors(error: Collection<MgrError>) = errors.addAll(error)

inline fun MgrContext.fail(error: MgrError) {
    addError(error)
    state = MgrState.FAILING
}

inline fun MgrContext.fail(errors: Collection<MgrError>) {
    addErrors(errors)
    state = MgrState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = MgrError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = MgrError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
package ru.demyanovaf.kotlin.taskManager.common.models

import ru.demyanovaf.kotlin.taskManager.logging.common.LogLevel

data class MgrError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)

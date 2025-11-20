package ru.demyanovaf.kotlin.taskManager.common

import ru.demyanovaf.kotlin.taskManager.logging.common.TmLoggerProvider

data class MgrCorSettings(
    val loggerProvider: TmLoggerProvider = TmLoggerProvider(),
) {
    companion object {
        val NONE = MgrCorSettings()
    }
}

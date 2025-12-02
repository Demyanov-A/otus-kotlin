package ru.demyanovaf.kotlin.taskManager.common

import ru.demyanovaf.kotlin.taskManager.common.ws.IMgrWsSessionRepo
import ru.demyanovaf.kotlin.taskManager.logging.common.TmLoggerProvider

data class MgrCorSettings(
    val loggerProvider: TmLoggerProvider = TmLoggerProvider(),
    val wsSessions: IMgrWsSessionRepo = IMgrWsSessionRepo.NONE,
) {
    companion object {
        val NONE = MgrCorSettings()
    }
}

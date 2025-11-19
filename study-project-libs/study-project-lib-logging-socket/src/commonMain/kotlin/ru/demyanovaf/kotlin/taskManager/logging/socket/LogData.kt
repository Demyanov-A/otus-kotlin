package ru.demyanovaf.kotlin.taskManager.logging.socket

import kotlinx.serialization.Serializable
import ru.demyanovaf.kotlin.taskManager.logging.common.LogLevel

@Serializable
data class LogData(
    val level: LogLevel,
    val message: String,
//    val data: T
)

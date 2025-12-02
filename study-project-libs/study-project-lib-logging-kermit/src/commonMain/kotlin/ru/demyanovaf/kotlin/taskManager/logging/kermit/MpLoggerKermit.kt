package ru.demyanovaf.kotlin.taskManager.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import ru.demyanovaf.kotlin.taskManager.logging.common.ITmLogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun tmLoggerKermit(loggerId: String): ITmLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return TmLoggerWrapperKermit(
        logger = logger,
        loggerId = loggerId,
    )
}

@Suppress("unused")
fun tmLoggerKermit(cls: KClass<*>): ITmLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return TmLoggerWrapperKermit(
        logger = logger,
        loggerId = cls.qualifiedName?: "",
    )
}

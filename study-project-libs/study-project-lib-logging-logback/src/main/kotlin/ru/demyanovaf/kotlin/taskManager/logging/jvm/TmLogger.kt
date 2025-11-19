package ru.demyanovaf.kotlin.taskManager.logging.jvm

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.demyanovaf.kotlin.taskManager.logging.common.ITmLogWrapper
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun tmLoggerLogback(logger: Logger): ITmLogWrapper = TmLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun tmLoggerLogback(clazz: KClass<*>): ITmLogWrapper = tmLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun tmLoggerLogback(loggerId: String): ITmLogWrapper = tmLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)

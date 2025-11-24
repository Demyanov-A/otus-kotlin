package ru.demyanovaf.kotlin.taskManager.app.common

import kotlinx.datetime.Clock
import ru.demyanovaf.kotlin.taskManager.api.log1.mapper.toLog
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.helpers.asMgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import kotlin.reflect.KClass

suspend inline fun <T> IMgrAppSettings.controllerHelper(
    crossinline getRequest: suspend MgrContext.() -> Unit,
    crossinline toResponse: suspend MgrContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MgrContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = MgrState.FAILING
        ctx.errors.add(e.asMgrError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}

package ru.demyanovaf.kotlin.taskManager.app.rabbit

import kotlinx.coroutines.runBlocking
import ru.demyanovaf.kotlin.taskManager.app.rabbit.config.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.app.rabbit.config.RabbitConfig
import ru.demyanovaf.kotlin.taskManager.app.rabbit.mappers.fromArgs
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.logging.common.TmLoggerProvider
import ru.demyanovaf.kotlin.taskManager.logging.jvm.tmLoggerLogback

fun main(vararg args: String) = runBlocking {
    val appSettings = MgrAppSettings(
        rabbit = RabbitConfig.fromArgs(*args),
        corSettings = MgrCorSettings(
            loggerProvider = TmLoggerProvider { tmLoggerLogback(it) }
        )
    )
    val app = RabbitApp(appSettings = appSettings, this)
    app.start()
}

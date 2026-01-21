package ru.demyanovaf.kotlin.taskManager.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.demyanovaf.kotlin.taskManager.app.spring.base.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.app.spring.base.SpringWsSessionRepo
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.logging.common.TmLoggerProvider
import ru.demyanovaf.kotlin.taskManager.logging.jvm.tmLoggerLogback

@Suppress("unused")
@Configuration
class TaskConfig {
    @Bean
    fun processor(corSettings: MgrCorSettings) = MgrTaskProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): TmLoggerProvider = TmLoggerProvider { tmLoggerLogback(it) }

    @Bean
    fun corSettings(): MgrCorSettings = MgrCorSettings(
        loggerProvider = loggerProvider(),
        wsSessions = wsRepo(),
    )

    @Bean
    fun appSettings(
        corSettings: MgrCorSettings,
        processor: MgrTaskProcessor,
    ) = MgrAppSettings(
        corSettings = corSettings,
        processor = processor,
    )

    @Bean
    fun wsRepo(): SpringWsSessionRepo = SpringWsSessionRepo()

}

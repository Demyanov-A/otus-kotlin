package ru.demyanovaf.kotlin.taskManager.app.spring.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.demyanovaf.kotlin.taskManager.app.spring.base.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.app.spring.base.SpringWsSessionRepo
import ru.demyanovaf.kotlin.taskManager.backend.repo.postgresql.RepoTaskSql
import ru.demyanovaf.kotlin.taskManager.backend.repository.inmemory.TaskRepoStub
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import ru.demyanovaf.kotlin.taskManager.logging.common.TmLoggerProvider
import ru.demyanovaf.kotlin.taskManager.logging.jvm.tmLoggerLogback
import ru.demyanovaf.kotlin.taskManager.repo.inmemory.TaskRepoInMemory

@Suppress("unused")
@EnableConfigurationProperties(TaskConfigPostgres::class)
@Configuration
class TaskConfig (val postgresConfig: TaskConfigPostgres) {

    val logger: Logger = LoggerFactory.getLogger(TaskConfig::class.java)
    @Bean
    fun processor(corSettings: MgrCorSettings) = MgrTaskProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): TmLoggerProvider = TmLoggerProvider { tmLoggerLogback(it) }

    @Bean
    fun corSettings(): MgrCorSettings = MgrCorSettings(
        loggerProvider = loggerProvider(),
        wsSessions = wsRepo(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
    )

    @Bean
    fun testRepo(): IRepoTask = TaskRepoInMemory()
    @Bean
    fun prodRepo(): IRepoTask = RepoTaskSql(postgresConfig.psql).apply {
        logger.info("Connecting to DB with $this")
    }
    @Bean
    fun stubRepo(): IRepoTask = TaskRepoStub()

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

package ru.demyanovaf.kotlin.taskManager.biz.stubs

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker
import ru.demyanovaf.kotlin.taskManager.logging.common.LogLevel
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub

fun ICorChainDsl<MgrContext>.stubReadSuccess(title: String, corSettings: MgrCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для чтения задачи
    """.trimIndent()
    on { stubCase == MgrStubs.SUCCESS && state == MgrState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubReadSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MgrState.FINISHING
            val stub = MgrTaskStub.prepareResult {
                taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            }
            taskResponse = stub
        }
    }
}

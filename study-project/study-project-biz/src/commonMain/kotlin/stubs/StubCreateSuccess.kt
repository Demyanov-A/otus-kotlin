package ru.demyanovaf.kotlin.taskManager.biz.stubs

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker
import ru.demyanovaf.kotlin.taskManager.logging.common.LogLevel
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub

fun ICorChainDsl<MgrContext>.stubCreateSuccess(title: String, corSettings: MgrCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания задачи
    """.trimIndent()
    on { stubCase == MgrStubs.SUCCESS && state == MgrState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MgrState.FINISHING
            val stub = MgrTaskStub.prepareResult {
                taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                taskRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                taskRequest.status.takeIf { it != MgrStatus.NONE }?.also { this.status = it }
                taskRequest.category.takeIf { it != MgrCategory.NONE }?.also { this.category = it }
            }
            taskResponse = stub
        }
    }
}

package ru.demyanovaf.kotlin.taskManager.biz.validation

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker

fun ICorChainDsl<MgrContext>.finishTaskValidation(title: String) = worker {
    this.title = title
    on { state == MgrState.RUNNING }
    handle {
            taskValidated = taskValidating
    }
}

fun ICorChainDsl<MgrContext>.finishTaskFilterValidation(title: String) = worker {
    this.title = title
    on { state == MgrState.RUNNING }
    handle {
        taskFilterValidated = taskFilterValidating
    }
}

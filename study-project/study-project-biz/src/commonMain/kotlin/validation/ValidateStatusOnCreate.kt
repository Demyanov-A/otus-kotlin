package ru.demyanovaf.kotlin.taskManager.biz.validation

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker

fun ICorChainDsl<MgrContext>.validateStatusOnCreate(title: String) = worker {
    this.title = title
    on { taskValidating.status != MgrStatus.NEW && workMode == MgrWorkMode.PROD}
    handle {
        taskValidating.status = MgrStatus.NEW
    }
}
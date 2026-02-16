package ru.demyanovaf.kotlin.taskManager.biz.validation

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker

fun ICorChainDsl<MgrContext>.validateDtCreateOnCreate(title: String) = worker {
    this.title = title
    on { taskValidating.dtCreate == Instant.NONE && workMode == MgrWorkMode.PROD}
    handle {
        taskValidating.dtCreate = Clock.System.now()
    }
}
package ru.demyanovaf.kotlin.taskManager.biz.validation

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.chain

fun ICorChainDsl<MgrContext>.validation(block: ICorChainDsl<MgrContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == MgrState.RUNNING }
}

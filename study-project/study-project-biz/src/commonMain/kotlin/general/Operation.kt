package ru.demyanovaf.kotlin.taskManager.biz.general

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.chain

fun ICorChainDsl<MgrContext>.operation(
    title: String,
    command: MgrCommand,
    block: ICorChainDsl<MgrContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == MgrState.RUNNING }
}

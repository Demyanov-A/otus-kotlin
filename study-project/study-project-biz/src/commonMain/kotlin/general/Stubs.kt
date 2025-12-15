package ru.demyanovaf.kotlin.taskManager.biz.general

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.chain

fun ICorChainDsl<MgrContext>.stubs(title: String, block: ICorChainDsl<MgrContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MgrWorkMode.STUB && state == MgrState.RUNNING }
}

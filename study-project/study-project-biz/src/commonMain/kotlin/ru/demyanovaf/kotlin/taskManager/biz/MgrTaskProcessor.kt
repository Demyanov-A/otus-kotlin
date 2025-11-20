package ru.demyanovaf.kotlin.taskManager.biz

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub

@Suppress("unused", "RedundantSuspendModifier")
class MgrTaskProcessor(val corSettings: MgrCorSettings) {
    suspend fun exec(ctx: MgrContext) {
        ctx.taskResponse = MgrTaskStub.get()
        ctx.state = MgrState.RUNNING
    }
}

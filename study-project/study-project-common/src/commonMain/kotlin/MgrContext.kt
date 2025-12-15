package ru.demyanovaf.kotlin.taskManager.common

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrRequestId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskFilter
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import ru.demyanovaf.kotlin.taskManager.common.ws.IMgrWsSession

data class MgrContext(
    var command: MgrCommand = MgrCommand.NONE,
    var state: MgrState = MgrState.NONE,
    val errors: MutableList<MgrError> = mutableListOf(),

    var corSettings: MgrCorSettings = MgrCorSettings(),
    var workMode: MgrWorkMode = MgrWorkMode.PROD,
    var stubCase: MgrStubs = MgrStubs.NONE,
    var wsSession: IMgrWsSession = IMgrWsSession.NONE,

    var requestId: MgrRequestId = MgrRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var taskRequest: MgrTask = MgrTask(),
    var taskFilterRequest: MgrTaskFilter = MgrTaskFilter(),

    var taskResponse: MgrTask = MgrTask(),
    var tasksResponse: MutableList<MgrTask> = mutableListOf(),
)

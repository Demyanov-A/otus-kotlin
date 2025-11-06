package ru.demyanovaf.kotlin.taskManager.common

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.models.*
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs

data class MgrContext(
    var command: MgrCommand = MgrCommand.NONE,
    var state: MgrState = MgrState.NONE,
    val errors: MutableList<MgrError> = mutableListOf(),

    var workMode: MgrWorkMode = MgrWorkMode.PROD,
    var stubCase: MgrStubs = MgrStubs.NONE,

    var requestId: MgrRequestId = MgrRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var taskRequest: MgrTask = MgrTask(),
    var taskFilterRequest: MgrTaskFilter = MgrTaskFilter(),

    var taskResponse: MgrTask = MgrTask(),
    var tasksResponse: MutableList<MgrTask> = mutableListOf(),
)

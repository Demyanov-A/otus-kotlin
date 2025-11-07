package ru.demyanovaf.kotlin.taskManager.stubs

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_CANCELED_LOW
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_DONE_LOW
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_IN_PROGRESS_LOW
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_NEW_LOW
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_TODO_LOW
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_HOLD_LOW

object MgrTaskStub {
    fun get(): MgrTask = TASK_NEW_LOW.copy()

    fun prepareResult(block: MgrTask.() -> Unit): MgrTask = get().apply(block)

    fun prepareSearchList(filter: String, type: MgrStatus) = listOf(
        mgrTaskNewLow("d-666-01", filter, type),
        mgrTaskNewLow("d-666-02", filter, type),
        mgrTaskNewLow("d-666-03", filter, type),
        mgrTaskNewLow("d-666-04", filter, type),
        mgrTaskNewLow("d-666-05", filter, type),
        mgrTaskNewLow("d-666-06", filter, type),
    )

    private fun mgrTaskNewLow(id: String, filter: String, type: MgrStatus) =
        mgrTask(TASK_NEW_LOW, id = id, filter = filter, type = type)

    private fun mgrTaskToDoLow(id: String, filter: String, type: MgrStatus) =
        mgrTask(TASK_TODO_LOW, id = id, filter = filter, type = type)

    private fun mgrTaskInProgressLow(id: String, filter: String, type: MgrStatus) =
        mgrTask(TASK_IN_PROGRESS_LOW, id = id, filter = filter, type = type)

    private fun mgrTaskHoldLow(id: String, filter: String, type: MgrStatus) =
        mgrTask(TASK_HOLD_LOW, id = id, filter = filter, type = type)

    private fun mgrTaskDoneLow(id: String, filter: String, type: MgrStatus) =
        mgrTask(TASK_DONE_LOW, id = id, filter = filter, type = type)

    private fun mgrTaskCanceledLow(id: String, filter: String, type: MgrStatus) =
        mgrTask(TASK_CANCELED_LOW, id = id, filter = filter, type = type)

    private fun mgrTask(base: MgrTask, id: String, filter: String, type: MgrStatus) = base.copy(
        id = MgrTaskId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        status = type,
    )

}

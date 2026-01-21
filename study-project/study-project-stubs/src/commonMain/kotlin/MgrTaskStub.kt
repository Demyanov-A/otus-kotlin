package ru.demyanovaf.kotlin.taskManager.stubs

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_CANCELED_LOW
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_DONE_LOW
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_HOLD_LOW
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_IN_PROGRESS_LOW
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_NEW_LOW
import ru.demyanovaf.kotlin.taskManager.stubs.MgrStubTasks.TASK_TODO_LOW

object MgrTaskStub {
    fun get(): MgrTask = TASK_NEW_LOW.copy()

    fun prepareResult(block: MgrTask.() -> Unit): MgrTask = get().apply(block)

    fun prepareSearchList(filter: String, status: MgrStatus, category: MgrCategory, dtCreate: Instant) = listOf(
        mgrTaskNewLow("d-666-01", filter, status, category, dtCreate),
        mgrTaskToDoLow("d-666-02", filter, status, category, dtCreate),
        mgrTaskInProgressLow("d-666-03", filter, status, category, dtCreate),
        mgrTaskHoldLow("d-666-04", filter, status, category, dtCreate),
        mgrTaskDoneLow("d-666-05", filter, status, category, dtCreate),
        mgrTaskCanceledLow("d-666-06", filter, status, category, dtCreate),
    )

    private fun mgrTaskNewLow(id: String, filter: String, status: MgrStatus, category: MgrCategory, dtCreate: Instant) =
        mgrTask(TASK_NEW_LOW, id = id, filter = filter, status = status, category = category, dtCreate = dtCreate)

    private fun mgrTaskToDoLow(
        id: String,
        filter: String,
        status: MgrStatus,
        category: MgrCategory,
        dtCreate: Instant
    ) =
        mgrTask(TASK_TODO_LOW, id = id, filter = filter, status = status, category = category, dtCreate = dtCreate)

    private fun mgrTaskInProgressLow(
        id: String,
        filter: String,
        status: MgrStatus,
        category: MgrCategory,
        dtCreate: Instant
    ) =
        mgrTask(
            TASK_IN_PROGRESS_LOW,
            id = id,
            filter = filter,
            status = status,
            category = category,
            dtCreate = dtCreate
        )

    private fun mgrTaskHoldLow(
        id: String,
        filter: String,
        status: MgrStatus,
        category: MgrCategory,
        dtCreate: Instant
    ) =
        mgrTask(TASK_HOLD_LOW, id = id, filter = filter, status = status, category = category, dtCreate = dtCreate)

    private fun mgrTaskDoneLow(
        id: String,
        filter: String,
        status: MgrStatus,
        category: MgrCategory,
        dtCreate: Instant
    ) =
        mgrTask(TASK_DONE_LOW, id = id, filter = filter, status = status, category = category, dtCreate = dtCreate)

    private fun mgrTaskCanceledLow(
        id: String,
        filter: String,
        status: MgrStatus,
        category: MgrCategory,
        dtCreate: Instant
    ) =
        mgrTask(TASK_CANCELED_LOW, id = id, filter = filter, status = status, category = category, dtCreate = dtCreate)

    private fun mgrTask(
        base: MgrTask,
        id: String,
        filter: String,
        status: MgrStatus,
        category: MgrCategory,
        dtCreate: Instant
    ) = base.copy(
        id = MgrTaskId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        status = status,
        category = category,
        dtCreate = dtCreate,
    )

}

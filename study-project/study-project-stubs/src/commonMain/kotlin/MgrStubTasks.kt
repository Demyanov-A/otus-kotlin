package ru.demyanovaf.kotlin.taskManager.stubs

import kotlinx.datetime.Clock
import ru.demyanovaf.kotlin.taskManager.common.models.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object MgrStubTasks {
    val TASK_NEW_LOW: MgrTask
        get() = MgrTask(
            id = MgrTaskId("666"),
            title = "Задача №1",
            description = "Требуется выполнить задачу 1",
            dtCreate = Clock.System.now(),
            deadline = Clock.System.now()
                .plus(259200L.toDuration(DurationUnit.SECONDS)),
            userId = MgrUserId("user-1"),
            category = MgrCategory.LOW,
            status = MgrStatus.NEW,
            lock = MgrTaskLock("123"),
            permissionsClient = mutableSetOf(
                MgrTaskPermissionClient.READ,
                MgrTaskPermissionClient.UPDATE,
                MgrTaskPermissionClient.DELETE,
            )
        )
    val TASK_TODO_LOW = TASK_NEW_LOW.copy(status = MgrStatus.TODO)
    val TASK_IN_PROGRESS_LOW = TASK_NEW_LOW.copy(status = MgrStatus.IN_PROGRESS)
    val TASK_HOLD_LOW = TASK_NEW_LOW.copy(status = MgrStatus.HOLD)
    val TASK_CANCELED_LOW = TASK_NEW_LOW.copy(status = MgrStatus.CANCELED)
    val TASK_DONE_LOW = TASK_NEW_LOW.copy(status = MgrStatus.DONE)
}

package ru.demyanovaf.kotlin.taskManager.stubs

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskPermissionClient
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId

object MgrStubTasks {
    val TASK_NEW_LOW: MgrTask
        get() = MgrTask(
            id = MgrTaskId("666"),
            title = "Задача №1",
            description = "Требуется выполнить задачу 1",
            userId = MgrUserId("user-1"),
            category = MgrCategory.LOW,
            status = MgrStatus.NEW,
            deadline = Instant.NONE,
            dtCreate = Instant.NONE,
            lock = MgrTaskLock("123"),
            permissionsClient = mutableSetOf(
                MgrTaskPermissionClient.READ,
                MgrTaskPermissionClient.UPDATE,
                MgrTaskPermissionClient.DELETE,
            )
        )
    val TASK_TODO_LOW = TASK_NEW_LOW.copy(status = MgrStatus.TO_DO)
    val TASK_IN_PROGRESS_LOW = TASK_NEW_LOW.copy(status = MgrStatus.IN_PROGRESS)
    val TASK_HOLD_LOW = TASK_NEW_LOW.copy(status = MgrStatus.HOLD)
    val TASK_CANCELED_LOW = TASK_NEW_LOW.copy(status = MgrStatus.CANCELED)
    val TASK_DONE_LOW = TASK_NEW_LOW.copy(status = MgrStatus.DONE)
}

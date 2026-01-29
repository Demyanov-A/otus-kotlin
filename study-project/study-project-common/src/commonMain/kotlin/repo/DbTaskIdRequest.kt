package ru.demyanovaf.kotlin.taskManager.common.repo

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock

data class DbTaskIdRequest(
    val id: MgrTaskId,
    val lock: MgrTaskLock = MgrTaskLock.NONE,
) {
    constructor(task: MgrTask): this(task.id, task.lock)
}

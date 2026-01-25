package ru.demyanovaf.kotlin.taskManager.common.repo

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId

data class DbTaskIdRequest(
    val id: MgrTaskId,
) {
    constructor(task: MgrTask): this(task.id)
}

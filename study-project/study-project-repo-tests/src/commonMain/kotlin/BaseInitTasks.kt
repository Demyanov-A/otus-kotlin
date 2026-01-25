package ru.demyanovaf.kotlin.taskManager.backend.repo.tests

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId

abstract class BaseInitTasks(private val op: String): IInitObjects<MgrTask> {
    fun createInitTestModel(
        suf: String,
        userId: MgrUserId = MgrUserId("user-123"),
        status: MgrStatus = MgrStatus.NEW,
        category: MgrCategory = MgrCategory.LOW,
        deadline: Instant = Instant.NONE,
        dtCreate: Instant = Instant.NONE,
    ) = MgrTask(
        id = MgrTaskId("task-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        userId = userId,
        category = category,
        status = status,
        deadline = deadline,
        dtCreate = dtCreate,
    )
}

package ru.demyanovaf.kotlin.taskManager.api.v2.mappers

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskReadObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskUpdateObject
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId

fun MgrTask.toTransportCreate() = TaskCreateObject(
    title = title.takeIf { it.isNotBlank() },
    userId = userId.takeIf { it != MgrUserId.NONE }?.asString(),
    description = description.takeIf { it.isNotBlank() },
    status = status.toTransportTask(),
    category = category.toTransportTask(),
    deadline = deadline.takeIf { it != Instant.NONE }?.toString(),
    dtCreate = dtCreate.takeIf { it != Instant.NONE }?.toString(),
)

fun MgrTask.toTransportRead() = TaskReadObject(
    id = id.takeIf { it != MgrTaskId.NONE }?.asString(),
)

fun MgrTask.toTransportUpdate() = TaskUpdateObject(
    id = id.takeIf { it != MgrTaskId.NONE }?.asString(),
    userId = userId.takeIf { it != MgrUserId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    status = status.toTransportTask(),
    category = category.toTransportTask(),
    deadline = deadline.takeIf { it != Instant.NONE }?.toString(),
    lock = lock.takeIf { it != MgrTaskLock.NONE }?.asString(),
)

fun MgrTask.toTransportDelete() = TaskDeleteObject(
    id = id.takeIf { it != MgrTaskId.NONE }?.asString(),
    lock = lock.takeIf { it != MgrTaskLock.NONE }?.asString(),
)

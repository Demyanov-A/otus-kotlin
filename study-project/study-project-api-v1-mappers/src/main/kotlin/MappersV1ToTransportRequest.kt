package ru.demyanovaf.kotlin.taskManager.mappers.v1

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskReadObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskUpdateObject
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock

fun MgrTask.toTransportCreateTask() = TaskCreateObject(
    title = title,
    description = description,
    status = status.toTransportTask(),
    category = category.toTransportTask(),
    deadline = deadline.takeIf { it != Instant.NONE }?.toString(),
    dtCreate = dtCreate.takeIf { it != Instant.NONE }?.toString(),
)

fun MgrTask.toTransportReadTask() = TaskReadObject(
    id = id.toTransportTask()
)

fun MgrTask.toTransportUpdateTask() = TaskUpdateObject(
    id = id.toTransportTask(),
    title = title,
    description = description,
    status = status.toTransportTask(),
    category = category.toTransportTask(),
    deadline = deadline.takeIf { it != Instant.NONE }?.toString(),
    dtCreate = dtCreate.takeIf { it != Instant.NONE }?.toString(),
    lock = lock.toTransportTask(),
)

internal fun MgrTaskLock.toTransportTask() = takeIf { it != MgrTaskLock.NONE }?.asString()

fun MgrTask.toTransportDeleteTask() = TaskDeleteObject(
    id = id.toTransportTask(),
    lock = lock.toTransportTask(),
)
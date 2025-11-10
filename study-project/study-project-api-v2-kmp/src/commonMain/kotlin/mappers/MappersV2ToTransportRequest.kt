package ru.demyanovaf.kotlin.taskManager.api.v2.mappers

import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskReadObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskUpdateObject
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock

fun MgrTask.toTransportCreateTask() = TaskCreateObject(
    title = title,
    description = description,
    category = category.toTransportTask(),
    status = status.toTransportTask(),
)

fun MgrTask.toTransportReadTask() = TaskReadObject(
    id = id.toTransportTask()
)

fun MgrTask.toTransportUpdateTask() = TaskUpdateObject(
    id = id.toTransportTask(),
    title = title,
    description = description,
    category = category.toTransportTask(),
    status = status.toTransportTask(),
    lock = lock.toTransportTask(),
)

internal fun MgrTaskLock.toTransportTask() = takeIf { it != MgrTaskLock.NONE }?.asString()

fun MgrTask.toTransportDeleteTask() = TaskDeleteObject(
    id = id.toTransportTask(),
    lock = lock.toTransportTask(),
)
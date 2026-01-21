package ru.demyanovaf.kotlin.taskManager.api.v2.mappers

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Error
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.ResponseResult
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskInitResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskPermissions
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskReadResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskResponseObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskSearchResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskUpdateResponse
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.exceptions.UnknownMgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskPermissionClient
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId

fun MgrContext.toTransportTask(): IResponse = when (val cmd = command) {
    MgrCommand.CREATE -> toTransportCreate()
    MgrCommand.READ -> toTransportRead()
    MgrCommand.UPDATE -> toTransportUpdate()
    MgrCommand.DELETE -> toTransportDelete()
    MgrCommand.SEARCH -> toTransportSearch()
    MgrCommand.INIT -> toTransportInit()
    MgrCommand.FINISH -> throw UnknownMgrCommand(cmd)
    MgrCommand.NONE -> throw UnknownMgrCommand(cmd)
}

fun MgrContext.toTransportCreate() = TaskCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MgrContext.toTransportRead() = TaskReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MgrContext.toTransportUpdate() = TaskUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MgrContext.toTransportDelete() = TaskDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    task = taskResponse.toTransportTask()
)

fun MgrContext.toTransportSearch() = TaskSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    tasks = tasksResponse.toTransportTask()
)

fun MgrContext.toTransportInit() = TaskInitResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun List<MgrTask>.toTransportTask(): List<TaskResponseObject>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

internal fun MgrTask.toTransportTask(): TaskResponseObject = TaskResponseObject(
    id = id.toTransportTask(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    userId = userId.takeIf { it != MgrUserId.NONE }?.asString(),
    category = category.toTransportTask(),
    status = status.toTransportTask(),
    deadline = deadline.takeIf { it != Instant.NONE }?.toString(),
    dtCreate = dtCreate.takeIf { it != Instant.NONE }?.toString(),
    permissions = permissionsClient.toTransportTask(),
    lock = lock.toTransportTask(),
)

internal fun MgrTaskId.toTransportTask() = takeIf { it != MgrTaskId.NONE }?.asString()

private fun Set<MgrTaskPermissionClient>.toTransportTask(): Set<TaskPermissions>? = this
    .map { it.toTransportTask() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MgrTaskPermissionClient.toTransportTask() = when (this) {
    MgrTaskPermissionClient.READ -> TaskPermissions.READ
    MgrTaskPermissionClient.UPDATE -> TaskPermissions.UPDATE
    MgrTaskPermissionClient.DELETE -> TaskPermissions.DELETE
}

internal fun MgrStatus.toTransportTask(): Status? = when (this) {
    MgrStatus.NEW -> Status.NEW
    MgrStatus.TO_DO -> Status.TO_DO
    MgrStatus.IN_PROGRESS -> Status.IN_PROGRESS
    MgrStatus.HOLD -> Status.HOLD
    MgrStatus.DONE -> Status.DONE
    MgrStatus.CANCELED -> Status.CANCELED
    MgrStatus.NONE -> null
}

internal fun MgrCategory.toTransportTask(): Category? = when (this) {
    MgrCategory.LOW -> Category.LOW
    MgrCategory.MEDIUM -> Category.MEDIUM
    MgrCategory.HI -> Category.HI
    MgrCategory.PERSONAL -> Category.PERSONAL
    MgrCategory.NONE -> null
}

private fun List<MgrError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportTask() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MgrError.toTransportTask() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun MgrState.toResult(): ResponseResult? = when (this) {
    MgrState.RUNNING, MgrState.FINISHING -> ResponseResult.SUCCESS
    MgrState.FAILING -> ResponseResult.ERROR
    MgrState.NONE -> null
}

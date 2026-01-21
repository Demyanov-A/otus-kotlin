package ru.demyanovaf.kotlin.taskManager.mappers.v1

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import ru.demyanovaf.kotlin.taskManager.api.v1.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskReadObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskReadRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskRequestDebugStubs
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchFilter
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskUpdateObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskUpdateRequest
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskFilter
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import ru.demyanovaf.kotlin.taskManager.mappers.v1.exceptions.UnknownRequestClass

fun MgrContext.fromTransport(request: IRequest) = when (request) {
    is TaskCreateRequest -> fromTransport(request)
    is TaskReadRequest -> fromTransport(request)
    is TaskUpdateRequest -> fromTransport(request)
    is TaskDeleteRequest -> fromTransport(request)
    is TaskSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toTaskId() = this?.let { MgrTaskId(it) } ?: MgrTaskId.NONE
private fun String?.toTaskLock() = this?.let { MgrTaskLock(it) } ?: MgrTaskLock.NONE
private fun String?.toMgrUserId() = this?.let { MgrUserId(it) } ?: MgrUserId.NONE

private fun TaskDebug?.transportToWorkMode(): MgrWorkMode = when (this?.mode) {
    TaskRequestDebugMode.PROD -> MgrWorkMode.PROD
    TaskRequestDebugMode.TEST -> MgrWorkMode.TEST
    TaskRequestDebugMode.STUB -> MgrWorkMode.STUB
    null -> MgrWorkMode.PROD
}

private fun TaskDebug?.transportToStubCase(): MgrStubs = when (this?.stub) {
    TaskRequestDebugStubs.SUCCESS -> MgrStubs.SUCCESS
    TaskRequestDebugStubs.NOT_FOUND -> MgrStubs.NOT_FOUND
    TaskRequestDebugStubs.BAD_ID -> MgrStubs.BAD_ID
    TaskRequestDebugStubs.BAD_TITLE -> MgrStubs.BAD_TITLE
    TaskRequestDebugStubs.BAD_DESCRIPTION -> MgrStubs.BAD_DESCRIPTION
    TaskRequestDebugStubs.BAD_CATEGORY -> MgrStubs.BAD_CATEGORY
    TaskRequestDebugStubs.BAD_STATUS -> MgrStubs.BAD_STATUS
    TaskRequestDebugStubs.CANNOT_DELETE -> MgrStubs.CANNOT_DELETE
    TaskRequestDebugStubs.BAD_SEARCH_STRING -> MgrStubs.BAD_SEARCH_STRING
    null -> MgrStubs.NONE
}

fun MgrContext.fromTransport(request: TaskReadRequest) {
    command = MgrCommand.READ
    taskRequest = request.task.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TaskReadObject?.toInternal(): MgrTask = if (this != null) {
    MgrTask(id = id.toTaskId())
} else {
    MgrTask()
}

fun MgrContext.fromTransport(request: TaskCreateRequest) {
    command = MgrCommand.CREATE
    taskRequest = request.task?.toInternal() ?: MgrTask()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MgrContext.fromTransport(request: TaskUpdateRequest) {
    command = MgrCommand.UPDATE
    taskRequest = request.task?.toInternal() ?: MgrTask()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MgrContext.fromTransport(request: TaskDeleteRequest) {
    command = MgrCommand.DELETE
    taskRequest = request.task.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TaskDeleteObject?.toInternal(): MgrTask = if (this != null) {
    MgrTask(
        id = id.toTaskId(),
        lock = lock.toTaskLock(),
    )
} else {
    MgrTask()
}

fun MgrContext.fromTransport(request: TaskSearchRequest) {
    command = MgrCommand.SEARCH
    taskFilterRequest = request.taskFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TaskSearchFilter?.toInternal(): MgrTaskFilter = MgrTaskFilter(
    searchString = this?.searchString ?: ""
)

private fun TaskCreateObject.toInternal(): MgrTask = MgrTask(
    userId = userId.toMgrUserId(),
    title = this.title ?: "",
    description = this.description ?: "",
    category = this.category.fromTransport(),
    deadline = try {
        this.deadline?.toInstant() ?: Instant.NONE
    } catch (_: Exception) {
        Instant.NONE
    },
    status = MgrStatus.NEW,
    dtCreate = Clock.System.now()
)

private fun TaskUpdateObject.toInternal(): MgrTask = MgrTask(
    id = this.id.toTaskId(),
    userId = userId.toMgrUserId(),
    title = this.title ?: "",
    description = this.description ?: "",
    category = this.category.fromTransport(),
    status = this.status.fromTransport(),
    deadline = try {
        this.deadline?.toInstant() ?: Instant.NONE
    } catch (_: Exception) {
        Instant.NONE
    },
    lock = this.lock.toTaskLock(),
)

private fun Category?.fromTransport(): MgrCategory = when (this) {
    Category.LOW -> MgrCategory.LOW
    Category.MEDIUM -> MgrCategory.MEDIUM
    Category.HI -> MgrCategory.HI
    Category.PERSONAL -> MgrCategory.PERSONAL
    null -> MgrCategory.NONE
}

private fun Status?.fromTransport(): MgrStatus = when (this) {
    Status.NEW -> MgrStatus.NEW
    Status.TO_DO -> MgrStatus.TO_DO
    Status.IN_PROGRESS -> MgrStatus.IN_PROGRESS
    Status.HOLD -> MgrStatus.HOLD
    Status.DONE -> MgrStatus.HOLD
    Status.CANCELED -> MgrStatus.CANCELED
    null -> MgrStatus.NONE
}


package ru.demyanovaf.kotlin.taskManager.api.v2.mappers

import ru.demyanovaf.kotlin.taskManager.api.v2.models.*
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskFilter
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs

fun MgrContext.fromTransport(request: IRequest) = when (request) {
    is TaskCreateRequest -> fromTransport(request)
    is TaskReadRequest -> fromTransport(request)
    is TaskUpdateRequest -> fromTransport(request)
    is TaskDeleteRequest -> fromTransport(request)
    is TaskSearchRequest -> fromTransport(request)
}

private fun String?.toTaskId() = this?.let { MgrTaskId(it) } ?: MgrTaskId.NONE
private fun String?.toTaskLock() = this?.let { MgrTaskLock(it) } ?: MgrTaskLock.NONE
private fun TaskReadObject?.toInternal() = if (this != null) {
    MgrTask(id = id.toTaskId())
} else {
    MgrTask()
}

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

fun MgrContext.fromTransport(request: TaskCreateRequest) {
    command = MgrCommand.CREATE
    taskRequest = request.task?.toInternal() ?: MgrTask()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MgrContext.fromTransport(request: TaskReadRequest) {
    command = MgrCommand.READ
    taskRequest = request.task.toInternal()
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
    title = this.title ?: "",
    description = this.description ?: "",
    category = this.category.fromTransport(),
    status = this.status.fromTransport(),
)

private fun TaskUpdateObject.toInternal(): MgrTask = MgrTask(
    id = this.id.toTaskId(),
    title = this.title ?: "",
    description = this.description ?: "",
    category = this.category.fromTransport(),
    status = this.status.fromTransport(),
    lock = this.lock.toTaskLock(),
)

private fun Status?.fromTransport(): MgrStatus = when (this) {
    Status.NEW -> MgrStatus.NEW
    Status.TO_DO -> MgrStatus.TO_DO
    Status.IN_PROGRESS -> MgrStatus.IN_PROGRESS
    Status.HOLD -> MgrStatus.HOLD
    Status.DONE -> MgrStatus.HOLD
    Status.CANCELED -> MgrStatus.CANCELED
    null -> MgrStatus.NONE
}

private fun Category?.fromTransport(): MgrCategory = when (this) {
    Category.LOW -> MgrCategory.LOW
    Category.MIDDLE -> MgrCategory.MIDDLE
    Category.HI -> MgrCategory.HI
    Category.PERSONAL -> MgrCategory.PERSONAL
    null -> MgrCategory.NONE
}

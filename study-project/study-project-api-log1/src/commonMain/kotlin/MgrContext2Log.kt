package ru.demyanovaf.kotlin.taskManager.api.log1.mapper

import kotlinx.datetime.Clock
import ru.demyanovaf.kotlin.taskManager.api.log1.models.CommonLogModel
import ru.demyanovaf.kotlin.taskManager.api.log1.models.ErrorLogModel
import ru.demyanovaf.kotlin.taskManager.api.log1.models.MgrLogModel
import ru.demyanovaf.kotlin.taskManager.api.log1.models.TaskFilterLog
import ru.demyanovaf.kotlin.taskManager.api.log1.models.TaskLog
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrRequestId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskFilter
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId

fun MgrContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "study-project",
    task = toMgrLog(),
    errors = errors.map { it.toLog() },
)

private fun MgrContext.toMgrLog(): MgrLogModel? {
    val taskNone = MgrTask()
    return MgrLogModel(
        requestId = requestId.takeIf { it != MgrRequestId.NONE }?.asString(),
        requestTask = taskRequest.takeIf { it != taskNone }?.toLog(),
        responseTask = taskResponse.takeIf { it != taskNone }?.toLog(),
        responseTasks = tasksResponse.takeIf { it.isNotEmpty() }?.filter { it != taskNone }?.map { it.toLog() },
        requestFilter = taskFilterRequest.takeIf { it != MgrTaskFilter() }?.toLog(),
    ).takeIf { it != MgrLogModel() }
}

private fun MgrTaskFilter.toLog() = TaskFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    userId = userId.takeIf { it != MgrUserId.NONE }?.asString(),
    status = status.takeIf { it != MgrStatus.NONE }?.name,
)

private fun MgrError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun MgrTask.toLog() = TaskLog(
    id = id.takeIf { it != MgrTaskId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    status = status.takeIf { it != MgrStatus.NONE }?.name,
    category = category.takeIf { it != MgrCategory.NONE }?.name,
    userId = userId.takeIf { it != MgrUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)

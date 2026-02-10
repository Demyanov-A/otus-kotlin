package ru.demyanovaf.kotlin.taskManager.repo.inmemory

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId

data class TaskEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val userId: String? = null,
    val status: String? = null,
    val category: String? = null,
    val deadline: String? = null,
    val dtCreate: String? = null,
    val lock: String? = null,
) {
    constructor(model: MgrTask): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        userId = model.userId.asString().takeIf { it.isNotBlank() },
        status = model.status.takeIf { it != MgrStatus.NONE }?.name,
        category = model.category.takeIf { it != MgrCategory.NONE }?.name,
        deadline = model.deadline.takeIf { it != Instant.NONE }?.toString(),
        dtCreate = model.dtCreate.takeIf { it != Instant.NONE }?.toString(),
        lock = model.lock.asString().takeIf { it.isNotBlank() }
        // Не нужно сохранять permissions, потому что он ВЫЧИСЛЯЕМЫЙ, а не хранимый
    )

    fun toInternal() = MgrTask(
        id = id?.let { MgrTaskId(it) }?: MgrTaskId.NONE,
        title = title?: "",
        description = description?: "",
        userId = userId?.let { MgrUserId(it) }?: MgrUserId.NONE,
        status = status?.let { MgrStatus.valueOf(it) }?: MgrStatus.NONE,
        category = category?.let { MgrCategory.valueOf(it) }?: MgrCategory.NONE,
        deadline = try {
            deadline?.let { Instant.parse(it) } ?: Instant.NONE
        } catch (_: Exception) {
            Instant.NONE
        },
        dtCreate = try {
            dtCreate?.let { Instant.parse(it) } ?: Instant.NONE
        } catch (_: Exception) {
            Instant.NONE
        },
        lock = lock?.let { MgrTaskLock(it) } ?: MgrTaskLock.NONE,
    )
}

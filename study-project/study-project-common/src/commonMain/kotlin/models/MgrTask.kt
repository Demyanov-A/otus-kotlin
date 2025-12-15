package ru.demyanovaf.kotlin.taskManager.common.models

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.NONE

data class MgrTask(
    var id: MgrTaskId = MgrTaskId.NONE,
    var title: String = "",
    var description: String = "",
    var dtCreate: Instant = Instant.NONE,
    var deadline: Instant = Instant.NONE,
    var userId: MgrUserId = MgrUserId.NONE,
    var category: MgrCategory = MgrCategory.NONE,
    var status: MgrStatus = MgrStatus.NONE,
    var lock: MgrTaskLock = MgrTaskLock.NONE,
    val permissionsClient: MutableSet<MgrTaskPermissionClient> = mutableSetOf(),
) {
    fun deepCopy(): MgrTask = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MgrTask()
    }
}

package ru.demyanovaf.kotlin.taskManager.common.repo

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId

data class DbTaskFilterRequest(
    val titleFilter: String = "",
    val userId: MgrUserId = MgrUserId.NONE,
    val status: MgrStatus = MgrStatus.NONE,
    val category: MgrCategory = MgrCategory.NONE,
    val dtCreate: Instant = Instant.NONE,
    val deadline: Instant = Instant.NONE,
)

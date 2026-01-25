package ru.demyanovaf.kotlin.taskManager.common.repo

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask

data class DbTaskRequest(
    val task: MgrTask
)

package ru.demyanovaf.kotlin.taskManager.repo.common

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class TaskRepoInitialized(
    val repo: IRepoTaskInitializable,
    initObjects: Collection<MgrTask> = emptyList(),
) : IRepoTaskInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<MgrTask> = save(initObjects).toList()
}

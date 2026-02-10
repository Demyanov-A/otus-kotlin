package ru.demyanovaf.kotlin.taskManager.repo.common

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask

interface IRepoTaskInitializable: IRepoTask {
    fun save(tasks: Collection<MgrTask>) : Collection<MgrTask>
}

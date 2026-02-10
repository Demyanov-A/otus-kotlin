package ru.demyanovaf.kotlin.taskManager.biz.repo

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.helpers.fail
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskFilterRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTasksResponseErr
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTasksResponseOk
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker

fun ICorChainDsl<MgrContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск задач в БД по фильтру"
    on { state == MgrState.RUNNING }
    handle {
        val request = DbTaskFilterRequest(
            titleFilter = taskFilterValidated.searchString,
            userId = taskFilterValidated.userId,
            status = taskFilterValidated.status,
            category = taskFilterValidated.category,
            deadline = taskFilterValidated.deadline,
            dtCreate = taskFilterValidated.dtCreate,
        )
        when(val result = taskRepo.searchTask(request)) {
            is DbTasksResponseOk -> tasksRepoDone = result.data.toMutableList()
            is DbTasksResponseErr -> fail(result.errors)
        }
    }
}

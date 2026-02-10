package ru.demyanovaf.kotlin.taskManager.biz.repo

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.helpers.fail
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErr
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErrWithData
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker

fun ICorChainDsl<MgrContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление задачи в БД"
    on { state == MgrState.RUNNING }
    handle {
        val request = DbTaskRequest(taskRepoPrepare)
        when(val result = taskRepo.createTask(request)) {
            is DbTaskResponseOk -> taskRepoDone = result.data
            is DbTaskResponseErr -> fail(result.errors)
            is DbTaskResponseErrWithData -> {
                fail(result.errors)
                taskRepoDone = result.data
            }
        }
    }
}

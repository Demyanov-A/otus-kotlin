package ru.demyanovaf.kotlin.taskManager.biz.repo

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.helpers.fail
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskIdRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErr
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErrWithData
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker

fun ICorChainDsl<MgrContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление задачи из БД по ID"
    on { state == MgrState.RUNNING }
    handle {
        val request = DbTaskIdRequest(taskRepoPrepare)
        when(val result = taskRepo.deleteTask(request)) {
            is DbTaskResponseOk -> taskRepoDone = result.data
            is DbTaskResponseErr -> {
                fail(result.errors)
                taskRepoDone = taskRepoRead
            }
            is DbTaskResponseErrWithData -> {
                fail(result.errors)
                taskRepoDone = result.data
            }
        }
    }
}

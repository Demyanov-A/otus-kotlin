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

fun ICorChainDsl<MgrContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение задачи из БД"
    on { state == MgrState.RUNNING }
    handle {
        val request = DbTaskIdRequest(taskValidated)
        when(val result = taskRepo.readTask(request)) {
            is DbTaskResponseOk -> taskRepoRead = result.data
            is DbTaskResponseErr -> fail(result.errors)
            is DbTaskResponseErrWithData -> {
                fail(result.errors)
                taskRepoRead = result.data
            }
        }
    }
}

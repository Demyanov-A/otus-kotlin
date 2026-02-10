package ru.demyanovaf.kotlin.taskManager.backend.repository.inmemory

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskFilterRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskIdRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTasksResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.IDbTaskResponse
import ru.demyanovaf.kotlin.taskManager.common.repo.IDbTasksResponse
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub

class TaskRepoStub() : IRepoTask {
    override suspend fun createTask(rq: DbTaskRequest): IDbTaskResponse {
        return DbTaskResponseOk(
            data = MgrTaskStub.get(),
        )
    }

    override suspend fun readTask(rq: DbTaskIdRequest): IDbTaskResponse {
        return DbTaskResponseOk(
            data = MgrTaskStub.get(),
        )
    }

    override suspend fun updateTask(rq: DbTaskRequest): IDbTaskResponse {
        return DbTaskResponseOk(
            data = MgrTaskStub.get(),
        )
    }

    override suspend fun deleteTask(rq: DbTaskIdRequest): IDbTaskResponse {
        return DbTaskResponseOk(
            data = MgrTaskStub.get(),
        )
    }

    override suspend fun searchTask(rq: DbTaskFilterRequest): IDbTasksResponse {
        return DbTasksResponseOk(
            data = MgrTaskStub.prepareSearchList(filter = "", MgrStatus.NEW, MgrCategory.LOW, Instant.NONE),
        )
    }
}

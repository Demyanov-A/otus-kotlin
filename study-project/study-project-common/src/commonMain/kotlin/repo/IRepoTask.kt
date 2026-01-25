package ru.demyanovaf.kotlin.taskManager.common.repo

interface IRepoTask {
    suspend fun createTask(rq: DbTaskRequest): IDbTaskResponse
    suspend fun readTask(rq: DbTaskIdRequest): IDbTaskResponse
    suspend fun updateTask(rq: DbTaskRequest): IDbTaskResponse
    suspend fun deleteTask(rq: DbTaskIdRequest): IDbTaskResponse
    suspend fun searchTask(rq: DbTaskFilterRequest): IDbTasksResponse
    companion object {
        val NONE = object : IRepoTask {
            override suspend fun createTask(rq: DbTaskRequest): IDbTaskResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readTask(rq: DbTaskIdRequest): IDbTaskResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateTask(rq: DbTaskRequest): IDbTaskResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteTask(rq: DbTaskIdRequest): IDbTaskResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchTask(rq: DbTaskFilterRequest): IDbTasksResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}

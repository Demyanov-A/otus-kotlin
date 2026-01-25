package ru.demyanovaf.kotlin.taskManager.common.repo

import ru.demyanovaf.kotlin.taskManager.common.helpers.errorSystem

abstract class TaskRepoBase: IRepoTask {

    protected suspend fun tryTaskMethod(block: suspend () -> IDbTaskResponse) = try {
        block()
    } catch (e: Throwable) {
        DbTaskResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryTasksMethod(block: suspend () -> IDbTasksResponse) = try {
        block()
    } catch (e: Throwable) {
        DbTasksResponseErr(errorSystem("methodException", e = e))
    }

}

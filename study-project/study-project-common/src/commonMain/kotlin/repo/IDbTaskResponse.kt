package ru.demyanovaf.kotlin.taskManager.common.repo

import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask

sealed interface IDbTaskResponse: IDbResponse<MgrTask>

data class DbTaskResponseOk(
    val data: MgrTask
): IDbTaskResponse

data class DbTaskResponseErr(
    val errors: List<MgrError> = emptyList()
): IDbTaskResponse {
    constructor(err: MgrError): this(listOf(err))
}

data class DbTaskResponseErrWithData(
    val data: MgrTask,
    val errors: List<MgrError> = emptyList()
): IDbTaskResponse {
    constructor(task: MgrTask, err: MgrError): this(task, listOf(err))
}

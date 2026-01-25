package ru.demyanovaf.kotlin.taskManager.common.repo

import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask

sealed interface IDbTasksResponse: IDbResponse<List<MgrTask>>

data class DbTasksResponseOk(
    val data: List<MgrTask>
): IDbTasksResponse

@Suppress("unused")
data class DbTasksResponseErr(
    val errors: List<MgrError> = emptyList()
): IDbTasksResponse {
    constructor(err: MgrError): this(listOf(err))
}

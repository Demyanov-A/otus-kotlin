package ru.demyanovaf.kotlin.taskManager.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.updateReturning
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.helpers.asMgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskFilterRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskIdRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErr
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTasksResponseErr
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTasksResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.IDbTaskResponse
import ru.demyanovaf.kotlin.taskManager.common.repo.IDbTasksResponse
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import ru.demyanovaf.kotlin.taskManager.common.repo.errorEmptyId
import ru.demyanovaf.kotlin.taskManager.common.repo.errorNotFound
import ru.demyanovaf.kotlin.taskManager.common.repo.errorRepoConcurrency
import ru.demyanovaf.kotlin.taskManager.repo.common.IRepoTaskInitializable

class RepoTaskSql(
    properties: SqlProperties,
    private val randomUuid: () -> String = { uuid4().toString() }
) : IRepoTask, IRepoTaskInitializable {
    private val taskTable = TaskTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    fun clear(): Unit = transaction(conn) {
        taskTable.deleteAll()
    }

    private fun saveObj(task: MgrTask): MgrTask = transaction(conn) {
        val res = taskTable
            .insert {
                it.to(task, randomUuid)
            }
            .resultedValues
            ?.map { taskTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbTaskResponse): IDbTaskResponse =
        transactionWrapper(block) { DbTaskResponseErr(it.asMgrError()) }

    override fun save(tasks: Collection<MgrTask>): Collection<MgrTask> = tasks.map { saveObj(it) }
    override suspend fun createTask(rq: DbTaskRequest): IDbTaskResponse = transactionWrapper {
        DbTaskResponseOk(saveObj(rq.task))
    }

    private fun read(id: MgrTaskId): IDbTaskResponse {
        val res = taskTable.selectAll().where {
            taskTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbTaskResponseOk(taskTable.from(res))
    }

    override suspend fun readTask(rq: DbTaskIdRequest): IDbTaskResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: MgrTaskId,
        lock: MgrTaskLock,
        block: (MgrTask) -> IDbTaskResponse
    ): IDbTaskResponse =
        transactionWrapper {
            if (id == MgrTaskId.NONE) return@transactionWrapper errorEmptyId

            val current = taskTable.selectAll().where { taskTable.id eq id.asString() }
                .singleOrNull()
                ?.let { taskTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    override suspend fun updateTask(rq: DbTaskRequest): IDbTaskResponse = update(rq.task.id, rq.task.lock) {
        taskTable.updateReturning(where = { taskTable.id eq rq.task.id.asString() }) {
            it.to(rq.task.copy(lock = MgrTaskLock(randomUuid())), randomUuid)
        }.singleOrNull()
            ?.let { DbTaskResponseOk(taskTable.from(it)) }
            ?: errorNotFound(rq.task.id)
    }

    override suspend fun deleteTask(rq: DbTaskIdRequest): IDbTaskResponse = update(rq.id, rq.lock) {
        taskTable.deleteWhere { id eq rq.id.asString() }
        DbTaskResponseOk(it)
    }

    override suspend fun searchTask(rq: DbTaskFilterRequest): IDbTasksResponse =
        transactionWrapper({
            val res = taskTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.userId != MgrUserId.NONE) {
                        add(taskTable.user eq rq.userId.asString())
                    }
                    if (rq.status != MgrStatus.NONE) {
                        add(taskTable.status eq rq.status)
                    }
                    if (rq.category != MgrCategory.NONE) {
                        add(taskTable.category eq rq.category)
                    }
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (taskTable.title like "%${rq.titleFilter}%")
                                    or (taskTable.description like "%${rq.titleFilter}%")
                        )
                    }
                    if (rq.deadline != Instant.NONE) {
                        add(taskTable.deadline like "%${rq.deadline.toString().take(13)}%")
                    }
                    if (rq.dtCreate != Instant.NONE) {
                        add(taskTable.dtCreated like "%${rq.dtCreate.toString().take(13)}%")
                    }
                }.reduce { a, b -> a and b }
            }
            DbTasksResponseOk(data = res.map { taskTable.from(it) })
        }, {
            DbTasksResponseErr(it.asMgrError())
        })
}

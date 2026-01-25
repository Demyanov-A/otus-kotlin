package ru.demyanovaf.kotlin.taskManager.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskFilterRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskIdRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTasksResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.IDbTaskResponse
import ru.demyanovaf.kotlin.taskManager.common.repo.IDbTasksResponse
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import ru.demyanovaf.kotlin.taskManager.common.repo.TaskRepoBase
import ru.demyanovaf.kotlin.taskManager.common.repo.errorEmptyId
import ru.demyanovaf.kotlin.taskManager.common.repo.errorNotFound
import ru.demyanovaf.kotlin.taskManager.repo.common.IRepoTaskInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class TaskRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : TaskRepoBase(), IRepoTask, IRepoTaskInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, TaskEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(tasks: Collection<MgrTask>) = tasks.map { task ->
        val entity = TaskEntity(task)
        require(entity.id != null)
        cache.put(entity.id, entity)
        task
    }

    override suspend fun createTask(rq: DbTaskRequest): IDbTaskResponse = tryTaskMethod {
        val key = randomUuid()
        val task = rq.task.copy(id = MgrTaskId(key))
        val entity = TaskEntity(task)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbTaskResponseOk(task)
    }

    override suspend fun readTask(rq: DbTaskIdRequest): IDbTaskResponse = tryTaskMethod {
        val key = rq.id.takeIf { it != MgrTaskId.NONE }?.asString() ?: return@tryTaskMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbTaskResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateTask(rq: DbTaskRequest): IDbTaskResponse = tryTaskMethod {
        val rqTask = rq.task
        val id = rqTask.id.takeIf { it != MgrTaskId.NONE } ?: return@tryTaskMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldTask = cache.get(key)?.toInternal()
            when {
                oldTask == null -> errorNotFound(id)
                else -> {
                    val newTask = rqTask.copy()
                    val entity = TaskEntity(newTask)
                    cache.put(key, entity)
                    DbTaskResponseOk(newTask)
                }
            }
        }
    }


    override suspend fun deleteTask(rq: DbTaskIdRequest): IDbTaskResponse = tryTaskMethod {
        val id = rq.id.takeIf { it != MgrTaskId.NONE } ?: return@tryTaskMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldTask = cache.get(key)?.toInternal()
            when {
                oldTask == null -> errorNotFound(id)
                else -> {
                    cache.invalidate(key)
                    DbTaskResponseOk(oldTask)
                }
            }
        }
    }

    /**
     * Поиск задач по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchTask(rq: DbTaskFilterRequest): IDbTasksResponse = tryTasksMethod {
        val result: List<MgrTask> = cache.asMap().asSequence()
            .filter { entry ->
                rq.userId.takeIf { it != MgrUserId.NONE }?.let {
                    it.asString() == entry.value.userId
                } ?: true
            }
            .filter { entry ->
                rq.status.takeIf { it != MgrStatus.NONE }?.let {
                    it.name == entry.value.status
                } ?: true
            }
            .filter { entry ->
                rq.category.takeIf { it != MgrCategory.NONE }?.let {
                    it.name == entry.value.category
                } ?: true
            }
            .filter { entry ->
                rq.deadline.takeIf { it != Instant.NONE }?.let{
                    it.toString().take(10) == entry.value.deadline?.take(10)
                } ?: true
            }
            .filter { entry ->
                rq.dtCreate.takeIf { it != Instant.NONE }?.let{
                    it.toString().take(10) == entry.value.dtCreate?.take(10)
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbTasksResponseOk(result)
    }
}

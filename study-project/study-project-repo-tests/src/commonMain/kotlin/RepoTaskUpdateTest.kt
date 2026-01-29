package ru.demyanovaf.kotlin.taskManager.backend.repo.tests

import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErr
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErrWithData
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoTaskUpdateTest {
    abstract val repo: IRepoTask
    protected open val updateSuccess = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = MgrTaskId("task-repo-update-not-found")
    protected val lockBad = MgrTaskLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = MgrTaskLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSuccess by lazy {
        MgrTask(
            id = updateSuccess.id,
            title = "update object",
            description = "update object description",
            userId = MgrUserId("user-123"),
            category = MgrCategory.LOW,
            status = MgrStatus.NEW,
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = MgrTask(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        userId = MgrUserId("user-123"),
        category = MgrCategory.LOW,
        status = MgrStatus.NEW,
        lock = initObjects.first().lock,
    )

    private val reqUpdateConc by lazy {
        MgrTask(
            id = updateConc.id,
            title = "update object not found",
            description = "update object not found description",
            userId = MgrUserId("user-123"),
            category = MgrCategory.LOW,
            status = MgrStatus.NEW,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateSuccess))
        assertIs<DbTaskResponseOk>(result)
        assertEquals(reqUpdateSuccess.id, result.data.id)
        assertEquals(reqUpdateSuccess.title, result.data.title)
        assertEquals(reqUpdateSuccess.description, result.data.description)
        assertEquals(reqUpdateSuccess.status, result.data.status)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateNotFound))
        assertIs<DbTaskResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateTask(DbTaskRequest(reqUpdateConc))
        assertIs<DbTaskResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitTasks("update") {
        override val initObjects: List<MgrTask> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}

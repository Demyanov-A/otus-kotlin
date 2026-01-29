package ru.demyanovaf.kotlin.taskManager.backend.repo.tests

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskIdRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErr
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErrWithData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoTaskDeleteTest {
    abstract val repo: IRepoTask
    protected open val deleteSuccess = initObjects[0]
    protected open val deleteConcurrency = initObjects[1]
    protected open val notFoundId = MgrTaskId("task-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSuccess.lock
        val result = repo.deleteTask(DbTaskIdRequest(deleteSuccess.id, lock = lockOld))
        assertIs<DbTaskResponseOk>(result)
        assertEquals(deleteSuccess.title, result.data.title)
        assertEquals(deleteSuccess.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readTask(DbTaskIdRequest(notFoundId, lock = lockOld))

        assertIs<DbTaskResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteTask(DbTaskIdRequest(deleteConcurrency.id, lock = lockBad))

        assertIs<DbTaskResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitTasks("delete") {
        override val initObjects: List<MgrTask> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}

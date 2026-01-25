package ru.demyanovaf.kotlin.taskManager.backend.repo.tests

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskIdRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErr
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoTaskReadTest {
    abstract val repo: IRepoTask
    protected open val readSuccess = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readTask(DbTaskIdRequest(readSuccess.id))

        assertIs<DbTaskResponseOk>(result)
        assertEquals(readSuccess, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readTask(DbTaskIdRequest(notFoundId))

        assertIs<DbTaskResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitTasks("delete") {
        override val initObjects: List<MgrTask> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = MgrTaskId("task-repo-read-notFound")

    }
}

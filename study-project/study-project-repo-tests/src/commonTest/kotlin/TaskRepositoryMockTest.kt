package ru.demyanovaf.kotlin.taskManager.backend.repo.tests

import kotlinx.coroutines.test.runTest
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskFilterRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskIdRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTasksResponseOk
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TaskRepositoryMockTest {
    private val repo = TaskRepositoryMock(
        invokeCreateTask = { DbTaskResponseOk(MgrTaskStub.prepareResult { title = "create" }) },
        invokeReadTask = { DbTaskResponseOk(MgrTaskStub.prepareResult { title = "read" }) },
        invokeUpdateTask = { DbTaskResponseOk(MgrTaskStub.prepareResult { title = "update" }) },
        invokeDeleteTask = { DbTaskResponseOk(MgrTaskStub.prepareResult { title = "delete" }) },
        invokeSearchTask = { DbTasksResponseOk(listOf(MgrTaskStub.prepareResult { title = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createTask(DbTaskRequest(MgrTask()))
        assertIs<DbTaskResponseOk>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readTask(DbTaskIdRequest(MgrTask()))
        assertIs<DbTaskResponseOk>(result)
        assertEquals("read", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateTask(DbTaskRequest(MgrTask()))
        assertIs<DbTaskResponseOk>(result)
        assertEquals("update", result.data.title)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteTask(DbTaskIdRequest(MgrTask()))
        assertIs<DbTaskResponseOk>(result)
        assertEquals("delete", result.data.title)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchTask(DbTaskFilterRequest())
        assertIs<DbTasksResponseOk>(result)
        assertEquals("search", result.data.first().title)
    }

}

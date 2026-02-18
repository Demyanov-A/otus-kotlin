package ru.demyanovaf.kotlin.taskManager.backend.repo.tests

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskFilterRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTasksResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoTaskSearchTest {
    abstract val repo: IRepoTask

    protected open val initializedObjects: List<MgrTask> = initObjects

    @Test
    fun searchUser() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(userId = searchUserId))
        assertIs<DbTasksResponseOk>(result)
        val expected = initializedObjects.filter { it.userId == searchUserId }.sortedBy { it.id.asString() }
        assertEquals(expected, result.data.filter { it.userId == searchUserId }.sortedBy { it.id.asString() })
    }

    @Test
    fun searchStatus() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(status = MgrStatus.NEW))
        assertIs<DbTasksResponseOk>(result)
        val expected = initializedObjects
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchCategory() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(category = MgrCategory.PERSONAL))
        assertIs<DbTasksResponseOk>(result)
        val expected = initializedObjects.filter { it.category == MgrCategory.PERSONAL }.sortedBy { it.id.asString() }
        assertEquals(expected, result.data.filter { it.category == MgrCategory.PERSONAL }.sortedBy { it.id.asString() })
    }

    @Test
    fun searchDeadline() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(deadline = Instant.NONE))
        assertIs<DbTasksResponseOk>(result)
        val expected = initializedObjects.sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchDtCreate() = runRepoTest {
        val result = repo.searchTask(DbTaskFilterRequest(dtCreate = Instant.NONE))
        assertIs<DbTasksResponseOk>(result)
        val expected = initializedObjects.sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitTasks("search") {

        val searchUserId = MgrUserId("user-124")
        override val initObjects: List<MgrTask> = listOf(
            createInitTestModel("task1"),
            createInitTestModel("task2", userId = searchUserId),
            createInitTestModel("task3", status = MgrStatus.NEW, category = MgrCategory.PERSONAL),
            createInitTestModel("task4", userId = searchUserId),
            createInitTestModel("task5", status = MgrStatus.NEW, category = MgrCategory.PERSONAL),
        )
    }
}

package ru.demyanovaf.kotlin.taskManager.backend.repo.tests

import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.repo.common.IRepoTaskInitializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals


abstract class RepoTaskCreateTest {
    abstract val repo: IRepoTaskInitializable
    protected open val uuidNew = MgrTaskId("10000000-0000-0000-0000-000000000001")

    private val createObj = MgrTask(
        title = "create object",
        description = "create object description",
        userId = MgrUserId("user-123"),
        category = MgrCategory.LOW,
        status = MgrStatus.NEW,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createTask(DbTaskRequest(createObj))
        val expected = createObj
        assertIs<DbTaskResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.description, result.data.description)
        assertEquals(expected.status, result.data.status)
        assertNotEquals(MgrTaskId.NONE, result.data.id)
    }

    companion object : BaseInitTasks("create") {
        override val initObjects: List<MgrTask> = emptyList()
    }
}

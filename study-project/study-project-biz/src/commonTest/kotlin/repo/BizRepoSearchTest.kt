package ru.demyanovaf.kotlin.taskManager.biz.repo

import kotlinx.coroutines.test.runTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.TaskRepositoryMock
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskFilter
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTasksResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = MgrUserId("321")
    private val command = MgrCommand.SEARCH
    private val initTask = MgrTask(
        id = MgrTaskId("123"),
        title = "abc",
        description = "abc",
        userId = userId,
        status = MgrStatus.NEW,
        category = MgrCategory.LOW,
    )
    private val repo = TaskRepositoryMock(
        invokeSearchTask = {
            DbTasksResponseOk(
                data = listOf(initTask),
            )
        }
    )
    private val settings = MgrCorSettings(repoTest = repo)
    private val processor = MgrTaskProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = MgrContext(
            command = command,
            state = MgrState.NONE,
            workMode = MgrWorkMode.TEST,
            taskFilterRequest = MgrTaskFilter(
                searchString = "abc",
                status = MgrStatus.NEW
            ),
        )
        processor.exec(ctx)
        assertEquals(MgrState.FINISHING, ctx.state)
        assertEquals(1, ctx.tasksResponse.size)
    }
}

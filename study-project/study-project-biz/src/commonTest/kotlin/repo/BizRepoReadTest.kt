package ru.demyanovaf.kotlin.taskManager.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.TaskRepositoryMock
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = MgrUserId("321")
    private val command = MgrCommand.READ
    private val initTask = MgrTask(
        id = MgrTaskId("123"),
        title = "abc",
        description = "abc",
        userId = userId,
        status = MgrStatus.NEW,
        category = MgrCategory.LOW,
    )
    private val repo = TaskRepositoryMock(
        invokeReadTask = {
            DbTaskResponseOk(
                data = initTask,
            )
        }
    )
    private val settings = MgrCorSettings(repoTest = repo)
    private val processor = MgrTaskProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = MgrContext(
            command = command,
            state = MgrState.NONE,
            workMode = MgrWorkMode.TEST,
            taskRequest = MgrTask(
                id = MgrTaskId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(MgrState.FINISHING, ctx.state)
        assertEquals(initTask.id, ctx.taskResponse.id)
        assertEquals(initTask.title, ctx.taskResponse.title)
        assertEquals(initTask.description, ctx.taskResponse.description)
        assertEquals(initTask.status, ctx.taskResponse.status)
        assertEquals(initTask.category, ctx.taskResponse.category)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}

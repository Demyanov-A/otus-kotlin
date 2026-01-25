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
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseErr
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = MgrUserId("321")
    private val command = MgrCommand.DELETE
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
        },
        invokeDeleteTask = {
            if (it.id == initTask.id)
                DbTaskResponseOk(
                    data = initTask
                )
            else DbTaskResponseErr()
        }
    )
    private val settings by lazy {
        MgrCorSettings(
            repoTest = repo
        )
    }
    private val processor = MgrTaskProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val adToUpdate = MgrTask(
            id = MgrTaskId("123"),
            lock = MgrTaskLock("123"),
        )
        val ctx = MgrContext(
            command = command,
            state = MgrState.NONE,
            workMode = MgrWorkMode.TEST,
            taskRequest = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(MgrState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initTask.id, ctx.taskResponse.id)
        assertEquals(initTask.title, ctx.taskResponse.title)
        assertEquals(initTask.description, ctx.taskResponse.description)
        assertEquals(initTask.status, ctx.taskResponse.status)
        assertEquals(initTask.category, ctx.taskResponse.category)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}

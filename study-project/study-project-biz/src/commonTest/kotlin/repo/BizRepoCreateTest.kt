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
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = MgrUserId("321")
    private val command = MgrCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = TaskRepositoryMock(
        invokeCreateTask = {
            DbTaskResponseOk(
                data = MgrTask(
                    id = MgrTaskId(uuid),
                    title = it.task.title,
                    description = it.task.description,
                    userId = userId,
                    status = it.task.status,
                    category = it.task.category,
                )
            )
        }
    )
    private val settings = MgrCorSettings(
        repoTest = repo
    )
    private val processor = MgrTaskProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = MgrContext(
            command = command,
            state = MgrState.NONE,
            workMode = MgrWorkMode.TEST,
            taskRequest = MgrTask(
                title = "abc",
                description = "abc",
                status = MgrStatus.NEW,
                category = MgrCategory.LOW,
            ),
        )
        processor.exec(ctx)
        assertEquals(MgrState.FINISHING, ctx.state)
        assertNotEquals(MgrTaskId.NONE, ctx.taskResponse.id)
        assertEquals("abc", ctx.taskResponse.title)
        assertEquals("abc", ctx.taskResponse.description)
        assertEquals(MgrStatus.NEW, ctx.taskResponse.status)
        assertEquals(MgrCategory.LOW, ctx.taskResponse.category)
    }
}

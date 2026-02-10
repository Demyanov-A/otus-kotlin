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
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUpdateTest {

    private val userId = MgrUserId("321")
    private val command = MgrCommand.UPDATE
    private val initTask = MgrTask(
        id = MgrTaskId("123"),
        title = "abc",
        description = "abc",
        userId = userId,
        status = MgrStatus.NEW,
        category = MgrCategory.LOW,
        lock = MgrTaskLock("123-234-abc-ABC"),
    )
    private val repo = TaskRepositoryMock(
        invokeReadTask = {
            DbTaskResponseOk(
                data = initTask,
            )
        },
        invokeUpdateTask = {
            DbTaskResponseOk(
                data = MgrTask(
                    id = MgrTaskId("123"),
                    title = "xyz",
                    description = "xyz",
                    status = MgrStatus.NEW,
                    category = MgrCategory.LOW,
                    lock = MgrTaskLock("123-234-abc-ABC"),
                )
            )
        }
    )
    private val settings = MgrCorSettings(repoTest = repo)
    private val processor = MgrTaskProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val adToUpdate = MgrTask(
            id = MgrTaskId("123"),
            title = "xyz",
            description = "xyz",
            status = MgrStatus.NEW,
            category = MgrCategory.LOW,
            lock = MgrTaskLock("123-234-abc-ABC"),
        )
        val ctx = MgrContext(
            command = command,
            state = MgrState.NONE,
            workMode = MgrWorkMode.TEST,
            taskRequest = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(MgrState.FINISHING, ctx.state)
        assertEquals(adToUpdate.id, ctx.taskResponse.id)
        assertEquals(adToUpdate.title, ctx.taskResponse.title)
        assertEquals(adToUpdate.description, ctx.taskResponse.description)
        assertEquals(adToUpdate.status, ctx.taskResponse.status)
        assertEquals(adToUpdate.category, ctx.taskResponse.category)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}

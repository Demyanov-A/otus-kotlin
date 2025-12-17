package ru.demyanovaf.kotlin.taskManager.biz.stub

import kotlinx.coroutines.test.runTest
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskReadStubTest {

    private val processor = MgrTaskProcessor()
    val id = MgrTaskId("666")

    @Test
    fun read() = runTest {

        val ctx = MgrContext(
            command = MgrCommand.READ,
            state = MgrState.NONE,
            workMode = MgrWorkMode.STUB,
            stubCase = MgrStubs.SUCCESS,
            taskRequest = MgrTask(
                id = id,
            ),
        )
        processor.exec(ctx)
        with(MgrTaskStub.get()) {
            assertEquals(id, ctx.taskResponse.id)
            assertEquals(title, ctx.taskResponse.title)
            assertEquals(description, ctx.taskResponse.description)
            assertEquals(status, ctx.taskResponse.status)
            assertEquals(category, ctx.taskResponse.category)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MgrContext(
            command = MgrCommand.READ,
            state = MgrState.NONE,
            workMode = MgrWorkMode.STUB,
            stubCase = MgrStubs.BAD_ID,
            taskRequest = MgrTask(),
        )
        processor.exec(ctx)
        assertEquals(MgrTask(), ctx.taskResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MgrContext(
            command = MgrCommand.READ,
            state = MgrState.NONE,
            workMode = MgrWorkMode.STUB,
            stubCase = MgrStubs.DB_ERROR,
            taskRequest = MgrTask(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MgrTask(), ctx.taskResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MgrContext(
            command = MgrCommand.READ,
            state = MgrState.NONE,
            workMode = MgrWorkMode.STUB,
            stubCase = MgrStubs.BAD_TITLE,
            taskRequest = MgrTask(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MgrTask(), ctx.taskResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}

package ru.demyanovaf.kotlin.taskManager.biz.stub

import kotlinx.coroutines.test.runTest
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskFilter
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class TaskSearchStubTest {

    private val processor = MgrTaskProcessor()
    val filter = MgrTaskFilter(searchString = "bolt")

    @Test
    fun read() = runTest {

        val ctx = MgrContext(
            command = MgrCommand.SEARCH,
            state = MgrState.NONE,
            workMode = MgrWorkMode.STUB,
            stubCase = MgrStubs.SUCCESS,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.tasksResponse.size > 1)
        val first = ctx.tasksResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with(MgrTaskStub.get()) {
            assertEquals(status, first.status)
            assertEquals(category, first.category)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MgrContext(
            command = MgrCommand.SEARCH,
            state = MgrState.NONE,
            workMode = MgrWorkMode.STUB,
            stubCase = MgrStubs.BAD_ID,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MgrTask(), ctx.taskResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MgrContext(
            command = MgrCommand.SEARCH,
            state = MgrState.NONE,
            workMode = MgrWorkMode.STUB,
            stubCase = MgrStubs.DB_ERROR,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MgrTask(), ctx.taskResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MgrContext(
            command = MgrCommand.SEARCH,
            state = MgrState.NONE,
            workMode = MgrWorkMode.STUB,
            stubCase = MgrStubs.BAD_TITLE,
            taskFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MgrTask(), ctx.taskResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}

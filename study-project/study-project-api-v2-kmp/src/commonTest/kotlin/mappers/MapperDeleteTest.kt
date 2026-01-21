package ru.demyanovaf.kotlin.taskManager.api.v2.mappers

import kotlinx.datetime.Clock
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugStubs
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrRequestId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperDeleteTest {
    @Test
    fun fromTransport() {
        val task = MgrTaskStub.get()
        val req = TaskDeleteRequest(
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS,
            ),
            task = MgrTaskStub.get().toTransportDeleteTask(),
        )

        val context = MgrContext()
        context.fromTransport(req)

        assertEquals(MgrStubs.SUCCESS, context.stubCase)
        assertEquals(MgrWorkMode.STUB, context.workMode)
        assertEquals(task.id.toTransportTask(), context.taskRequest.id.asString())
        assertEquals(task.lock.toTransportTask(), context.taskRequest.lock.asString())
    }

    @Test
    fun toTransport() {
        val dtCreate = Clock.System.now()
        val context = MgrContext(
            requestId = MgrRequestId("1234"),
            command = MgrCommand.DELETE,
            taskResponse = MgrTaskStub.prepareResult {
                this.dtCreate = dtCreate
            },
            errors = mutableListOf(
                MgrError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MgrState.RUNNING,
        )

        val req = context.toTransportTask() as TaskDeleteResponse
        val expected = MgrTaskStub.get()
        expected.dtCreate = dtCreate

        assertEquals(req.task, expected.toTransportTask())
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}

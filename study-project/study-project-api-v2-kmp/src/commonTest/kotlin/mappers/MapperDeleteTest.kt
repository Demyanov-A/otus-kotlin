package ru.demyanovaf.kotlin.taskManager.api.v2.mappers

import ru.demyanovaf.kotlin.taskManager.api.v2.models.*
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.*
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.assertEquals
import kotlin.test.Test

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
        val context = MgrContext(
            requestId = MgrRequestId("1234"),
            command = MgrCommand.DELETE,
            taskResponse = MgrTaskStub.get(),
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

        assertEquals(MgrTaskStub.get().toTransportTask(), req.task)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}

package ru.demyanovaf.kotlin.taskManager.api.v2.mappers

import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugStubs
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrRequestId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = TaskCreateRequest(
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS,
            ),
            task = MgrTaskStub.get().toTransportCreateTask(),
        )
        val expected = MgrTaskStub.prepareResult {
            this.id = MgrTaskId.NONE
            this.userId = MgrUserId.NONE
            this.lock = MgrTaskLock.NONE
            this.permissionsClient.clear()
        }

        val context = MgrContext()
        context.fromTransport(req)

        assertEquals(MgrStubs.SUCCESS, context.stubCase)
        assertEquals(MgrWorkMode.STUB, context.workMode)
        assertEquals(expected, context.taskRequest)
    }

    @Test
    fun toTransport() {
        val context = MgrContext(
            requestId = MgrRequestId("1234"),
            command = MgrCommand.CREATE,
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

        val req = context.toTransportTask() as TaskCreateResponse
        val expected = MgrTaskStub.get()

        assertEquals(req.task, expected.toTransportTask())
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}

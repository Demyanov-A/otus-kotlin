package ru.demyanovaf.kotlin.taskManager.api.v2.mappers

import kotlinx.datetime.Clock
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugStubs
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskUpdateRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskUpdateResponse
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrRequestId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperUpdateTest {
    @Test
    fun fromTransport() {
        val dtCreate = Clock.System.now()
        val req = TaskUpdateRequest(
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS,
            ),
            task = MgrTaskStub.get().toTransportUpdateTask(),
        )
        val expected = MgrTaskStub.prepareResult {
            this.userId = MgrUserId.NONE
            this.dtCreate = dtCreate
            this.permissionsClient.clear()
        }

        val context = MgrContext()
        context.fromTransport(req)
        context.taskRequest.dtCreate = dtCreate

        assertEquals(MgrStubs.SUCCESS, context.stubCase)
        assertEquals(MgrWorkMode.STUB, context.workMode)
        assertEquals(expected, context.taskRequest)
    }

    @Test
    fun toTransport() {
        val dtCreate = Clock.System.now()
        val context = MgrContext(
            requestId = MgrRequestId("1234"),
            command = MgrCommand.UPDATE,
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

        val req = context.toTransportTask() as TaskUpdateResponse
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

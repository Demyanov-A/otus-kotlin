package ru.demyanovaf.kotlin.taskManager.api.v2.mappers

import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugStubs
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperValidatedTest {
    @Test
    fun fromTransportValidated() {
        val req = TaskCreateRequest(
            debug = TaskDebug(
                stub = TaskRequestDebugStubs.SUCCESS,
            ),
        )

        val context = MgrContext()
        context.fromTransportValidated(req)

        assertEquals(MgrStubs.SUCCESS, context.stubCase)
    }
}
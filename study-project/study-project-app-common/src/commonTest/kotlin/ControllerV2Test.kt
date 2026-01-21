package ru.demyanovaf.kotlin.taskManager.app.common

import kotlinx.coroutines.test.runTest
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.fromTransport
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.toTransportTask
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.ResponseResult
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugStubs
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = TaskCreateRequest(
        task = TaskCreateObject(
            title = "some task",
            description = "some description of some task",
            status = Status.NEW,
            category = Category.LOW,
        ),
        debug = TaskDebug(mode = TaskRequestDebugMode.STUB, stub = TaskRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IMgrAppSettings = object : IMgrAppSettings {
        override val corSettings: MgrCorSettings = MgrCorSettings()
        override val processor: MgrTaskProcessor = MgrTaskProcessor(corSettings)
    }

    private suspend fun createAdSpring(request: TaskCreateRequest): TaskCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportTask() as TaskCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createAdKtor(appSettings: IMgrAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<TaskCreateRequest>()) },
            { toTransportTask() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createAdSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createAdKtor(appSettings) }
        val res = testApp.res as TaskCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}

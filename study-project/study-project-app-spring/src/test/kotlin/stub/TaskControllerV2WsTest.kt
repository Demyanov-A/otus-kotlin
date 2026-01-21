package ru.demyanovaf.kotlin.taskManager.app.spring.stub

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2RequestSerialize
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2ResponseDeserialize
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.ResponseResult
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskInitResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugStubs
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class TaskControllerV2WsTest : TaskControllerBaseWsTest<IRequest, IResponse>("v2") {

    @LocalServerPort
    override var port: Int = 0

    override fun deserializeRs(response: String): IResponse = apiV2ResponseDeserialize(response)
    override fun serializeRq(request: IRequest): String = apiV2RequestSerialize(request)

    @Test
    fun wsCreate(): Unit = testWsApp(
        TaskCreateRequest(
            debug = TaskDebug(TaskRequestDebugMode.STUB, TaskRequestDebugStubs.SUCCESS),
            task = TaskCreateObject(
                title = "test1",
                description = "desc",
                status = Status.NEW,
                category = Category.LOW,
            )
        )
    ) { pl ->
        val mesInit = pl[0]
        val mesCreate = pl[1]
        assert(mesInit is TaskInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesCreate is TaskCreateResponse)
        assert(mesCreate.result == ResponseResult.SUCCESS)
    }
}

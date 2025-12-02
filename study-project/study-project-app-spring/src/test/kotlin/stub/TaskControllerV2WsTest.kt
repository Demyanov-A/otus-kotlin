package ru.demyanovaf.kotlin.taskManager.app.spring.stub

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.demyanovaf.kotlin.taskManager.api.v2.models.*
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2RequestSerialize
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2ResponseDeserialize
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class TaskControllerV2WsTest: TaskControllerBaseWsTest<IRequest, IResponse>("v2") {

    @LocalServerPort
    override var port: Int = 0

    override fun deserializeRs(response: String): IResponse = apiV2ResponseDeserialize(response)
    override fun serializeRq(request: IRequest): String = apiV2RequestSerialize(request)

    @Test
    fun wsCreate(): Unit = testWsApp(TaskCreateRequest(
        debug = TaskDebug(TaskRequestDebugMode.STUB, TaskRequestDebugStubs.SUCCESS),
        task = TaskCreateObject(
            title = "test1",
            description = "desc",
            status = Status.NEW,
            category = Category.LOW,
        )
    )) { pl ->
        val mesInit = pl[0]
        val mesCreate = pl[1]
        assert(mesInit is TaskInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesCreate is TaskCreateResponse)
        assert(mesCreate.result == ResponseResult.SUCCESS)
    }
}

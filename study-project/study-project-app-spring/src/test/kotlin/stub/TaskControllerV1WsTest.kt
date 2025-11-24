package ru.demyanovaf.kotlin.taskManager.app.spring.stub

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1RequestSerialize
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1ResponseDeserialize
import ru.demyanovaf.kotlin.taskManager.api.v1.models.*
import ru.demyanovaf.kotlin.taskManager.api.v1.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v1.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateObject
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class TaskControllerV1WsTest: TaskControllerBaseWsTest<IRequest, IResponse>("v1") {

    @LocalServerPort
    override var port: Int = 0

    override fun deserializeRs(response: String): IResponse = apiV1ResponseDeserialize(response)
    override fun serializeRq(request: IRequest): String = apiV1RequestSerialize(request)

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

    @Test
    fun wsRead(): Unit = testWsApp(TaskReadRequest(
        debug = TaskDebug(TaskRequestDebugMode.STUB, TaskRequestDebugStubs.SUCCESS),
        task = TaskReadObject(
            id = "666"
        )
    )) { pl ->
        val mesInit = pl[0]
        val mesRead = pl[1]
        assert(mesInit is TaskInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesRead is TaskReadResponse)
        assert(mesRead.result == ResponseResult.SUCCESS)
    }

    @Test
    fun wsUpdate(): Unit = testWsApp(TaskUpdateRequest(
        debug = TaskDebug(TaskRequestDebugMode.STUB, TaskRequestDebugStubs.SUCCESS),
        task = TaskUpdateObject(
            id = "666",
            title = "xx",
            description = "yy",
            status = Status.NEW,
            category = Category.LOW,
            lock = "123",
        )
    )) { pl ->
        val mesInit = pl[0]
        val mesUpdate = pl[1]
        assert(mesInit is TaskInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesUpdate is TaskUpdateResponse)
        assert(mesUpdate.result == ResponseResult.SUCCESS)
    }
}

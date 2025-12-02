package ru.demyanovaf.kotlin.taskManager.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskReadRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskUpdateRequest
import ru.demyanovaf.kotlin.taskManager.app.spring.config.TaskConfig
import ru.demyanovaf.kotlin.taskManager.app.spring.controllers.TaskControllerV1Fine
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportCreate
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportDelete
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportRead
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportSearch
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportUpdate
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(TaskControllerV1Fine::class, TaskConfig::class)
internal class TaskControllerV1Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: MgrTaskProcessor

    @Test
    fun createTask() = testStubTask(
        "/v1/task/create",
        TaskCreateRequest(),
        MgrContext().toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readTask() = testStubTask(
        "/v1/task/read",
        TaskReadRequest(),
        MgrContext().toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateTask() = testStubTask(
        "/v1/task/update",
        TaskUpdateRequest(),
        MgrContext().toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteTask() = testStubTask(
        "/v1/task/delete",
        TaskDeleteRequest(),
        MgrContext().toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchTask() = testStubTask(
        "/v1/task/search",
        TaskSearchRequest(),
        MgrContext().toTransportSearch().copy(responseType = "search")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubTask(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}

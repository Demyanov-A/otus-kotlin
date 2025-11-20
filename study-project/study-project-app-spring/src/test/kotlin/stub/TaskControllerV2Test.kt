package ru.demyanovaf.kotlin.taskManager.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.toTransportCreate
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.toTransportDelete
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.toTransportRead
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.toTransportSearch
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.toTransportUpdate
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskReadRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskSearchRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskUpdateRequest
import ru.demyanovaf.kotlin.taskManager.app.spring.config.TaskConfig
import ru.demyanovaf.kotlin.taskManager.app.spring.controllers.TaskControllerV2Fine
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(TaskControllerV2Fine::class, TaskConfig::class)
internal class TaskControllerV2Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: MgrTaskProcessor

    @Test
    fun createTask() = testStubTask(
        "/v2/task/create",
        TaskCreateRequest(),
        MgrContext().toTransportCreate()
    )

    @Test
    fun readTask() = testStubTask(
        "/v2/task/read",
        TaskReadRequest(),
        MgrContext().toTransportRead()
    )

    @Test
    fun updateTask() = testStubTask(
        "/v2/task/update",
        TaskUpdateRequest(),
        MgrContext().toTransportUpdate()
    )

    @Test
    fun deleteTask() = testStubTask(
        "/v2/task/delete",
        TaskDeleteRequest(),
        MgrContext().toTransportDelete()
    )

    @Test
    fun searchTask() = testStubTask(
        "/v2/task/search",
        TaskSearchRequest(),
        MgrContext().toTransportSearch()
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

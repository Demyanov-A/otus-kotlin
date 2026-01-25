package ru.demyanovaf.kotlin.taskManager.app.spring.repo

import kotlinx.datetime.Instant
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskReadRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchFilter
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskUpdateRequest
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportCreate
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportDelete
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportRead
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportSearch
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportUpdate
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.Test

internal abstract class TaskRepoBaseV1Test {
    protected abstract var webClient: WebTestClient
    private val debug = TaskDebug(mode = TaskRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createTask() = testRepoTask(
        "create",
        TaskCreateRequest(
            task = MgrTaskStub.get().toTransportCreate(),
            debug = debug,
        ),
        prepareCtx(MgrTaskStub.prepareResult {
            id = MgrTaskId(uuidNew)
            lock = MgrTaskLock.NONE
        })
            .toTransportCreate()
            .copy(responseType = "create")
    )

    @Test
    open fun readTask() = testRepoTask(
        "read",
        TaskReadRequest(
            task = MgrTaskStub.get().toTransportRead(),
            debug = debug,
        ),
        prepareCtx(MgrTaskStub.get())
            .toTransportRead()
            .copy(responseType = "read")
    )

    @Test
    open fun updateTask() = testRepoTask(
        "update",
        TaskUpdateRequest(
            task = MgrTaskStub.prepareResult { title = "add" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(MgrTaskStub.prepareResult { title = "add" })
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    open fun deleteTask() = testRepoTask(
        "delete",
        TaskDeleteRequest(
            task = MgrTaskStub.get().toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(MgrTaskStub.get())
            .toTransportDelete()
            .copy(responseType = "delete")
    )

    @Test
    open fun searchTask() = testRepoTask(
        "search",
        TaskSearchRequest(
            taskFilter = TaskSearchFilter(status = Status.NEW),
            debug = debug,
        ),
        MgrContext(
            state = MgrState.RUNNING,
            tasksResponse = (MgrTaskStub.prepareSearchList("xx", MgrStatus.NEW, MgrCategory.LOW, Instant.NONE) + MgrTaskStub.get())
                .onEach { it.permissionsClient.clear() }
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch().copy(responseType = "search")
    )

    private fun prepareCtx(task: MgrTask) = MgrContext(
        state = MgrState.RUNNING,
        taskResponse = task.apply {
            // Пока не реализована эта функциональность
            permissionsClient.clear()
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoTask(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/task/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value { res ->
                println("RESPONSE: $res")
                val sortedResp: IResponse = when (res) {
                    is TaskSearchResponse -> res.copy(tasks = res.tasks?.sortedBy { it.id })
                    else -> res
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}

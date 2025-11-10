package ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.demyanovaf.kotlin.taskManager.api.v1.models.ResponseResult
import ru.demyanovaf.kotlin.taskManager.api.v1.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskResponseObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchFilter
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchResponse
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.client.Client
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1.base.sendAndReceive
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1.base.someCreateTask
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchV1(
    private val client: Client,
    private val debug: TaskDebug? = null
) {
    @Test
    fun search() = runBlocking {
        val objs = listOf(
            someCreateTask,
            someCreateTask.copy(status = Status.HOLD),
            someCreateTask.copy(status = Status.TO_DO),
        ).map { obj ->
            val resCreate = client.sendAndReceive(
                "task/create", TaskCreateRequest(
                    requestType = "create",
                    debug = debug,
                    task = obj,
                )
            ) as TaskCreateResponse

            assertEquals(ResponseResult.SUCCESS, resCreate.result)

            val cObj: TaskResponseObject = resCreate.task ?: fail("No task in Create response")
            assertEquals(obj.title, cObj.title)
            assertEquals(obj.description, cObj.description)
            assertEquals(obj.category, cObj.category)
            assertEquals(obj.status, cObj.status)
            cObj
        }

        val sObj = TaskSearchFilter(searchString = "new")
        val resSearch = client.sendAndReceive(
            "task/search",
            TaskSearchRequest(
                requestType = "search",
                debug = debug,
                taskFilter = sObj,
            )
        ) as TaskSearchResponse

        assertEquals(ResponseResult.SUCCESS, resSearch.result)

        val rsObj: List<TaskResponseObject> = resSearch.tasks ?: fail("No tasks in Search response")
        val statuses = rsObj.map { it.status }
        assertContains(statuses, Status.NEW)

        objs.forEach { obj ->
            val resDelete = client.sendAndReceive(
                "task/delete", TaskDeleteRequest(
                    requestType = "delete",
                    debug = debug,
                    task = TaskDeleteObject(obj.id, obj.lock),
                )
            ) as TaskDeleteResponse

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}
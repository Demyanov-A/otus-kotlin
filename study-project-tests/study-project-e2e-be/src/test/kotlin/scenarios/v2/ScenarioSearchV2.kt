package ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v2

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.demyanovaf.kotlin.taskManager.api.v2.models.ResponseResult
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskResponseObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskSearchFilter
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskSearchRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskSearchResponse
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.client.Client
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v2.base.sendAndReceive
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v2.base.someCreateTask
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchV2(
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
                    debug = debug,
                    task = TaskDeleteObject(obj.id, obj.lock),
                )
            ) as TaskDeleteResponse

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}
package ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.demyanovaf.kotlin.taskManager.api.v1.models.ResponseResult
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskReadObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskReadRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskReadResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskResponseObject
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.client.Client
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1.base.sendAndReceive
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1.base.someCreateTask
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioReadV1(
    private val client: Client,
    private val debug: TaskDebug? = null
) {
    @Test
    fun read() = runBlocking {
        val obj = someCreateTask
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

        val rObj = TaskReadObject(
            id = cObj.id,
        )
        val resRead = client.sendAndReceive(
            "task/read",
            TaskReadRequest(
                requestType = "read",
                debug = debug,
                task = rObj,
            )
        ) as TaskReadResponse

        assertEquals(ResponseResult.SUCCESS, resRead.result)

        val rrObj: TaskResponseObject = resRead.task ?: fail("No task in Read response")
        assertEquals(obj.title, rrObj.title)
        assertEquals(obj.description, rrObj.description)
        assertEquals(obj.category, rrObj.category)
        assertEquals(obj.status, rrObj.status)

        val resDelete = client.sendAndReceive(
            "task/delete", TaskDeleteRequest(
                requestType = "delete",
                debug = debug,
                task = TaskDeleteObject(cObj.id, cObj.lock),
            )
        ) as TaskDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)

        val dObj: TaskResponseObject = resDelete.task ?: fail("No task in Delete response")
        assertEquals(obj.title, dObj.title)
        assertEquals(obj.description, dObj.description)
        assertEquals(obj.category, dObj.category)
        assertEquals(obj.status, dObj.status)
    }
}
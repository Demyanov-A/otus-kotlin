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
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskResponseObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskUpdateObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskUpdateRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskUpdateResponse
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.client.Client
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1.base.sendAndReceive
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1.base.someCreateTask
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioUpdateV1(
    private val client: Client,
    private val debug: TaskDebug? = null
) {
    @Test
    fun update() = runBlocking {
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

        val uObj = TaskUpdateObject(
            id = cObj.id,
            lock = cObj.lock,
            title = "Задача №1",
            description = cObj.description,
            status = cObj.status,
            category = cObj.category,
        )
        val resUpdate = client.sendAndReceive(
            "task/update",
            TaskUpdateRequest(
                requestType = "update",
                debug = debug,
                task = uObj,
            )
        ) as TaskUpdateResponse

        assertEquals(ResponseResult.SUCCESS, resUpdate.result)

        val ruObj: TaskResponseObject = resUpdate.task ?: fail("No task in Update response")
        assertEquals(uObj.title, ruObj.title)
        assertEquals(uObj.description, ruObj.description)
        assertEquals(uObj.category, ruObj.category)
        assertEquals(uObj.status, ruObj.status)

        val resDelete = client.sendAndReceive(
            "task/delete", TaskDeleteRequest(
                requestType = "delete",
                debug = debug,
                task = TaskDeleteObject(ruObj.id, ruObj.lock),
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
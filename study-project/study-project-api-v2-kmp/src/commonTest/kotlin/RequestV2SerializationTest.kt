package ru.demyanovaf.kotlin.taskManager.api.v2

import ru.demyanovaf.kotlin.taskManager.api.v2.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugStubs
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {
    private val request: IRequest = TaskCreateRequest(
        debug = TaskDebug(
            mode = TaskRequestDebugMode.STUB,
            stub = TaskRequestDebugStubs.BAD_TITLE
        ),
        task = TaskCreateObject(
            title = "task title",
            description = "task description",
            status = Status.NEW,
            category = Category.LOW,
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"task title\""))
        assertContains(json, Regex("\"description\":\\s*\"task description\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
        assertContains(json, Regex("\"category\":\\s*\"low\""))
        assertContains(json, Regex("\"status\":\\s*\"new\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2Mapper.decodeFromString<IRequest>(json) as TaskCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"task": null}
        """.trimIndent()
        val obj = apiV2Mapper.decodeFromString<TaskCreateRequest>(jsonString)

        assertEquals(null, obj.task)
    }
}

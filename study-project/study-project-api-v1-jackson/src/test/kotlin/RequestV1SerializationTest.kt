package ru.demyanovaf.kotlin.taskManager.api.v1

import ru.demyanovaf.kotlin.taskManager.api.v1.models.*
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = TaskCreateRequest(
        debug = TaskDebug(
            mode = TaskRequestDebugMode.STUB,
            stub = TaskRequestDebugStubs.BAD_TITLE
        ),
        task = TaskCreateObject(
            title = "task title",
            description = "task description",
            dtCreate = Instant.now().toString(),
            deadline = Instant.now().plusSeconds(259200L).toString(),
            category = Category.LOW,
            status = Status.NEW
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

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
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as TaskCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"task": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, TaskCreateRequest::class.java)

        assertEquals(null, obj.task)
    }
}

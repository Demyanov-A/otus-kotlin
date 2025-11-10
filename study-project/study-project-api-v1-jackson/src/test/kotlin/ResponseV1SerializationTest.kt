package ru.demyanovaf.kotlin.taskManager.api.v1

import ru.demyanovaf.kotlin.taskManager.api.v1.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskResponseObject
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = TaskCreateResponse(
        task = TaskResponseObject(
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
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"task title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"description\":\\s*\"task description\""))
        assertContains(json, Regex("\"category\":\\s*\"low\""))
        assertContains(json, Regex("\"status\":\\s*\"new\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as TaskCreateResponse

        assertEquals(response, obj)
    }
}

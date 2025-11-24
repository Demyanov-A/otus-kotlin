package ru.demyanovaf.kotlin.taskManager.api.v2

import ru.demyanovaf.kotlin.taskManager.api.v2.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskResponseObject
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response: IResponse = TaskCreateResponse(
        task = TaskResponseObject(
            title = "task title",
            description = "task description",
            status = Status.NEW,
            category = Category.LOW,
        )
    )

    @Test
    fun serialize() {
//        val json = apiV2Mapper.encodeToString(TaskRequestSerializer1, request)
//        val json = apiV2Mapper.encodeToString(RequestSerializers.create, request)
        val json = apiV2Mapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"task title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"description\":\\s*\"task description\""))
        assertContains(json, Regex("\"category\":\\s*\"low\""))
        assertContains(json, Regex("\"status\":\\s*\"new\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2Mapper.decodeFromString<IResponse>(json) as TaskCreateResponse

        assertEquals(response, obj)
    }
}

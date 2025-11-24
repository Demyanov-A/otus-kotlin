package ru.demyanovaf.kotlin.taskManager.app.spring.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.fromTransport
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.toTransportTask
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDeleteResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskReadRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskReadResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskSearchRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskSearchResponse
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskUpdateRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskUpdateResponse
import ru.demyanovaf.kotlin.taskManager.app.common.controllerHelper
import ru.demyanovaf.kotlin.taskManager.app.spring.base.MgrAppSettings
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v2/task")
class TaskControllerV2Fine(private val appSettings: MgrAppSettings) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: TaskCreateRequest): TaskCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun read(@RequestBody request: TaskReadRequest): TaskReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun update(@RequestBody request: TaskUpdateRequest): TaskUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun delete(@RequestBody request: TaskDeleteRequest): TaskDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun search(@RequestBody request: TaskSearchRequest): TaskSearchResponse =
        process(appSettings, request = request, this::class, "search")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: MgrAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransportTask() as R },
            clazz,
            logId,
        )
    }
}

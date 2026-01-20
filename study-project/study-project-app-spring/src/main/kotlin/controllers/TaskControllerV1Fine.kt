package ru.demyanovaf.kotlin.taskManager.app.spring.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDeleteResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskReadRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskReadResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskSearchResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskUpdateRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskUpdateResponse
import ru.demyanovaf.kotlin.taskManager.app.common.controllerHelper
import ru.demyanovaf.kotlin.taskManager.app.spring.base.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.mappers.v1.fromTransport
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportTask
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/task")
class TaskControllerV1Fine(private val appSettings: MgrAppSettings) {

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

package ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1.base

import co.touchlab.kermit.Logger
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1RequestSerialize
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1ResponseDeserialize
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IResponse
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.client.Client

private val log = Logger

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiV1RequestSerialize(request)
    log.i { "Send to v1/$path\n$requestBody" }

    val responseBody = sendAndReceive("v1", path, requestBody)
    log.i { "Received\n$responseBody" }

    return apiV1ResponseDeserialize(responseBody)
}

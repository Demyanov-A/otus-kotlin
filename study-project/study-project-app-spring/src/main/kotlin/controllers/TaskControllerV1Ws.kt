package ru.demyanovaf.kotlin.taskManager.app.spring.controllers

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1Mapper
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IRequest
import ru.demyanovaf.kotlin.taskManager.app.common.controllerHelper
import ru.demyanovaf.kotlin.taskManager.app.spring.base.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.app.spring.base.SpringWsSessionV1
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.mappers.v1.fromTransport
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportTask

@Component
class TaskControllerV1Ws(private val appSettings: MgrAppSettings) : WebSocketHandler {
    private val sessions = appSettings.corSettings.wsSessions

    override fun handle(session: WebSocketSession): Mono<Void> {
        val MgrSess = SpringWsSessionV1(session)
        sessions.add(MgrSess)
        val messageObj = flow {
            emit(process("ws-v1-init") {
                command = MgrCommand.INIT
                wsSession = MgrSess
            })
        }

        val messages = session.receive().asFlow()
            .map { message ->
                process("ws-v1-handle") {
                    wsSession = MgrSess
                    val request = apiV1Mapper.readValue(message.payloadAsText, IRequest::class.java)
                    fromTransport(request)
                }
            }

        val output = merge(messageObj, messages)
            .onCompletion {
                process("ws-v1-finish") {
                    wsSession = MgrSess
                    command = MgrCommand.FINISH
                }
                sessions.remove(MgrSess)
            }
            .map { session.textMessage(apiV1Mapper.writeValueAsString(it)) }
            .asFlux()
        return session.send(output)
    }

    private suspend fun process(logId: String, function: MgrContext.() -> Unit) = appSettings.controllerHelper(
        getRequest = function,
        toResponse = MgrContext::toTransportTask,
        clazz = this@TaskControllerV1Ws::class,
        logId = logId,
    )
}

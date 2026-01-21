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
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2RequestDeserialize
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2ResponseSerialize
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.fromTransport
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.toTransportTask
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IRequest
import ru.demyanovaf.kotlin.taskManager.app.common.controllerHelper
import ru.demyanovaf.kotlin.taskManager.app.spring.base.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.app.spring.base.SpringWsSessionV2
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand

@Component
class TaskControllerV2Ws(private val appSettings: MgrAppSettings) : WebSocketHandler {
    private val sessions = appSettings.corSettings.wsSessions

    override fun handle(session: WebSocketSession): Mono<Void> {
        val MgrSess = SpringWsSessionV2(session)
        sessions.add(MgrSess)
        val messageObj = flow {
            emit(process("ws-v2-init") {
                command = MgrCommand.INIT
                wsSession = MgrSess
            })
        }

        val messages = session.receive().asFlow()
            .map { message ->
                process("ws-v2-handle") {
                    wsSession = MgrSess
                    val request = apiV2RequestDeserialize<IRequest>(message.payloadAsText)
                    fromTransport(request)
                }
            }

        val output = merge(messageObj, messages)
            .onCompletion {
                process("ws-v2-finish") {
                    command = MgrCommand.FINISH
                    wsSession = MgrSess
                }
                sessions.remove(MgrSess)
            }
            .map { session.textMessage(apiV2ResponseSerialize(it)) }
            .asFlux()
        return session.send(output)
    }

    private suspend fun process(logId: String, function: MgrContext.() -> Unit) = appSettings.controllerHelper(
        getRequest = function,
        toResponse = MgrContext::toTransportTask,
        clazz = this@TaskControllerV2Ws::class,
        logId = logId,
    )
}

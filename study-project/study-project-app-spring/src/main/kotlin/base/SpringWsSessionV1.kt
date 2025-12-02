package ru.demyanovaf.kotlin.taskManager.app.spring.base

import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1ResponseSerialize
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IResponse
import ru.demyanovaf.kotlin.taskManager.common.ws.IMgrWsSession

data class SpringWsSessionV1(
    private val session: WebSocketSession,
) : IMgrWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        val message = apiV1ResponseSerialize(obj)
        println("SENDING to WsV1: $message")
        session.send(Mono.just(session.textMessage(message)))
    }
}

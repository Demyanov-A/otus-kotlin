package ru.demyanovaf.kotlin.taskManager.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import ru.demyanovaf.kotlin.taskManager.app.spring.controllers.TaskControllerV1Ws
import ru.demyanovaf.kotlin.taskManager.app.spring.controllers.TaskControllerV2Ws


@Suppress("unused")
@Configuration
class WebSocketConfig(
    private val adControllerV1: TaskControllerV1Ws,
    private val adControllerV2: TaskControllerV2Ws,
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMap: Map<String, WebSocketHandler> = mapOf(
            "/v1/ws" to adControllerV1,
            "/v2/ws" to adControllerV2,
        )
        return SimpleUrlHandlerMapping(handlerMap, 1)
    }
}

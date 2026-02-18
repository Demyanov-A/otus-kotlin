package ru.demyanovaf.kotlin.taskManager.app.spring.base

import ru.demyanovaf.kotlin.taskManager.app.spring.config.KafkaConfig
import ru.demyanovaf.kotlin.taskManager.common.MgrContext

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: KafkaConfig): InputOutputTopics

    /**
     * Сериализатор для версии API
     */
    fun serialize(source: MgrContext): String

    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: MgrContext)
}

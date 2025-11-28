package ru.demyanovaf.kotlin.taskManager.app.kafka

import ru.demyanovaf.kotlin.taskManager.common.MgrContext

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: MgrContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: MgrContext)
}

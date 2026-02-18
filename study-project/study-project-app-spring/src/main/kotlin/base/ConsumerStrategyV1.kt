package ru.demyanovaf.kotlin.taskManager.app.spring.base

import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1RequestDeserialize
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1ResponseSerialize
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IResponse
import ru.demyanovaf.kotlin.taskManager.app.spring.config.KafkaConfig
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.mappers.v1.fromTransport
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportTask

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: KafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: MgrContext): String {
        val response: IResponse = source.toTransportTask()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MgrContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}

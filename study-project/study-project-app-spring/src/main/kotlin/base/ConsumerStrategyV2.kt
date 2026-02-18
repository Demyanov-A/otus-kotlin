package ru.demyanovaf.kotlin.taskManager.app.spring.base

import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2RequestDeserialize
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2ResponseSerialize
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.fromTransport
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.toTransportTask
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IRequest
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IResponse
import ru.demyanovaf.kotlin.taskManager.app.spring.config.KafkaConfig
import ru.demyanovaf.kotlin.taskManager.common.MgrContext

class ConsumerStrategyV2 : IConsumerStrategy {
    override fun topics(config: KafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun serialize(source: MgrContext): String {
        val response: IResponse = source.toTransportTask()
        return apiV2ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MgrContext) {
        val request: IRequest = apiV2RequestDeserialize(value)
        target.fromTransport(request)
    }
}

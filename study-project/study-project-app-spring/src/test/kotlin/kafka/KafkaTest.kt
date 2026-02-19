package ru.demyanovaf.kotlin.taskManager.app.kafka

import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Test
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1RequestSerialize
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1ResponseDeserialize
import ru.demyanovaf.kotlin.taskManager.api.v1.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v1.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskRequestDebugStubs
import ru.demyanovaf.kotlin.taskManager.app.spring.base.ConsumerStrategyV1
import ru.demyanovaf.kotlin.taskManager.app.spring.base.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.app.spring.config.KafkaConfig
import ru.demyanovaf.kotlin.taskManager.app.spring.kafka.KafkaConsumer
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import java.util.*
import kotlin.test.assertEquals

class KafkaTest {

    private var appSettings = MgrAppSettings(MgrCorSettings(), MgrTaskProcessor())

    @Test
    fun runKafka() = runBlocking {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer(true, StringSerializer(), StringSerializer())
        val config = KafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = KafkaConsumer(appSettings, listOf(ConsumerStrategyV1()), config, consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        TaskCreateRequest(
                            task = TaskCreateObject(
                                userId = "user-1",
                                title = "Задача №1",
                                description = "some testing ad to check them all",
                                status = Status.NEW,
                                category = Category.LOW,
                            ),
                            debug = TaskDebug(
                                mode = TaskRequestDebugMode.STUB,
                                stub = TaskRequestDebugStubs.SUCCESS,
                            ),
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.startSuspend()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<TaskCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("Задача №1", result.task?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}



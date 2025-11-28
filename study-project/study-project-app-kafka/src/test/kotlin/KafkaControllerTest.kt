package ru.demyanovaf.kotlin.taskManager.app.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
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
import java.util.*
import kotlin.test.assertEquals


class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
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

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<TaskCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("Задача №1", result.task?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}



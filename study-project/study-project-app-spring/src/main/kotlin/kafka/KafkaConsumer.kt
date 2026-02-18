package ru.demyanovaf.kotlin.taskManager.app.spring.kafka

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import ru.demyanovaf.kotlin.taskManager.app.common.controllerHelper
import ru.demyanovaf.kotlin.taskManager.app.spring.base.ConsumerStrategyV1
import ru.demyanovaf.kotlin.taskManager.app.spring.base.ConsumerStrategyV2
import ru.demyanovaf.kotlin.taskManager.app.spring.base.IConsumerStrategy
import ru.demyanovaf.kotlin.taskManager.app.spring.base.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.app.spring.config.KafkaConfig
import ru.demyanovaf.kotlin.taskManager.app.spring.config.createKafkaConsumer
import ru.demyanovaf.kotlin.taskManager.app.spring.config.createKafkaProducer
import java.time.Duration
import java.util.UUID
import kotlin.collections.forEach

@Suppress("unused")
@EnableConfigurationProperties(KafkaConfig::class)
@Component
class KafkaConsumer(
    private val appSettings: MgrAppSettings,
    private val config: KafkaConfig
) {
    private val consumerStrategies = listOf(ConsumerStrategyV1(), ConsumerStrategyV2())
    private val consumer: Consumer<String, String> = config.createKafkaConsumer()
    private val producer: Producer<String, String> = config.createKafkaProducer()
    private val log = appSettings.corSettings.loggerProvider.logger(this::class)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val process = atomic(true) // пояснить
    private val topicsAndStrategyByInputTopic: Map<String, TopicsAndStrategy> = consumerStrategies.associate {
        val topics = it.topics(config)
        topics.input to TopicsAndStrategy(topics.input, topics.output, it)
    }

    @Bean
    fun start() = scope.launch { startSuspend() }

    suspend fun startSuspend() {
        log.info("KafkaConsumer started")
        process.value = true
        try {
            consumer.subscribe(topicsAndStrategyByInputTopic.keys)
            while (process.value) {
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }
                if (!records.isEmpty)
                    log.debug("Receive ${records.count()} messages")

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        val (_, outputTopic, strategy) = topicsAndStrategyByInputTopic[record.topic()]
                            ?: throw RuntimeException("Receive message from unknown topic ${record.topic()}")

                        val resp = appSettings.controllerHelper(
                            { strategy.deserialize(record.value(), this) },
                            { strategy.serialize(this) },
                            KafkaConsumer::class,
                            "kafka-consumer"
                        )
                        sendResponse(resp, outputTopic)
                    } catch (ex: Exception) {
                        log.error("error", e = ex)
                    }
                }
            }
        } catch (ex: WakeupException) {
            // ignore for shutdown
        } catch (ex: RuntimeException) {
            // exception handling
            withContext(NonCancellable) {
                throw ex
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    private suspend fun sendResponse(json: String, outputTopic: String) {
        val resRecord = ProducerRecord(
            outputTopic,
//            null,
            UUID.randomUUID().toString(),
            json
        )
        log.info("sending ${resRecord.key()} to $outputTopic:\n$json")
        withContext(Dispatchers.IO) {
            producer.send(resRecord)
        }
    }

    private data class TopicsAndStrategy(
        val inputTopic: String,
        val outputTopic: String,
        val strategy: IConsumerStrategy
    )
}
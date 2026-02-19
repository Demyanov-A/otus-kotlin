package ru.demyanovaf.kotlin.taskManager.blackbox.fixture.client

import kotlinx.coroutines.delay
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.DockerCompose
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.client.Client
import java.time.Duration
import java.util.UUID

/**
 * Отправка запросов в очереди kafka
 */
class KafkaClient(dockerCompose: DockerCompose) : Client {
    private var d = 25000L
    private val host by lazy {
        val url = dockerCompose.inputUrl
        "${url.host}:${url.port}"
    }
    private val producer by lazy {
        KafkaProducer<String, String>(
            mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to host,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
            )
        )
    }
    private val consumer by lazy {
        KafkaConsumer<String, String>(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to host,
                ConsumerConfig.GROUP_ID_CONFIG to UUID.randomUUID().toString(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
            )
        ).also {
            it.subscribe(versions.map { "taskManager-task-$it-out" })
        }
    }
    private var counter = 0
    private val versions = setOf("v1", "v2")

    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        if (version !in versions) {
            throw UnsupportedOperationException("Unknown version $version")
        }

        delay(d)
        d = 0L

        counter += 1
        producer.send(ProducerRecord("taskManager-task-$version-in", "test-$counter", request)).get()

        val read = consumer.poll(Duration.ofSeconds(10))
        return read.firstOrNull()?.value() ?: ""
    }
}
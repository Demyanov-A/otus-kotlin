package ru.demyanovaf.kotlin.taskManager.app.rabbit

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.RabbitMQContainer
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1Mapper
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateObject
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateRequest
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateResponse
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskRequestDebugStubs
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2RequestSerialize
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2ResponseDeserialize
import ru.demyanovaf.kotlin.taskManager.app.rabbit.config.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.app.rabbit.config.RabbitConfig
import ru.demyanovaf.kotlin.taskManager.app.rabbit.config.RabbitExchangeConfiguration
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateObject as TaskCreateObjectV2
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateRequest as TaskCreateRequestV2
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateResponse as TaskCreateResponseV2
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug as TaskDebugV2
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugMode as TaskRequestDebugModeV2
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugStubs as TaskRequestDebugStubsV2

//  тесты с использованием testcontainers
internal class RabbitMqTest {

    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
        const val RMQ_PORT = 5672

        private val container = run {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
            RabbitMQContainer("rabbitmq:latest").apply {
//                withExposedPorts(5672, 15672) // Для 3-management
                withExposedPorts(RMQ_PORT)
            }
        }

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            container.start()
//            println("CONTAINER PORT (15672): ${container.getMappedPort(15672)}")
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            container.stop()
        }
    }

    private val appSettings = MgrAppSettings(
        rabbit = RabbitConfig(
            port = container.getMappedPort(RMQ_PORT)
        ),
//        corSettings = MgrCorSettings(loggerProvider = MpLoggerProvider { mpLoggerLogback(it) }),
        controllersConfigV1 = RabbitExchangeConfiguration(
            keyIn = "in-v1",
            keyOut = "out-v1",
            exchange = exchange,
            queue = "v1-queue",
            consumerTag = "v1-consumer-test",
            exchangeType = exchangeType
        ),
        controllersConfigV2 = RabbitExchangeConfiguration(
            keyIn = "in-v2",
            keyOut = "out-v2",
            exchange = exchange,
            queue = "v2-queue",
            consumerTag = "v2-consumer-test",
            exchangeType = exchangeType
        ),
    )
    private val app = RabbitApp(appSettings = appSettings)

    @BeforeTest
    fun tearUp() {
        app.start()
    }

    @AfterTest
    fun tearDown() {
        println("Test is being stopped")
        app.close()
    }

    @Test
    fun TaskCreateTestV1() {
        val (keyOut, keyIn) = with(appSettings.controllersConfigV1) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV1Mapper.writeValueAsBytes(boltCreateV1))

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV1Mapper.readValue(responseJson, TaskCreateResponse::class.java)
                val expected = MgrTaskStub.get()

                assertEquals(expected.title, response.task?.title)
                assertEquals(expected.description, response.task?.description)
            }
        }
    }

    @Test
    fun TaskCreateTestV2() {
        val (keyOut, keyIn) = with(appSettings.controllersConfigV2) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV2RequestSerialize(boltCreateV2).toByteArray())

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV2ResponseDeserialize<TaskCreateResponseV2>(responseJson)
                val expected = MgrTaskStub.get()
                assertEquals(expected.title, response.task?.title)
                assertEquals(expected.description, response.task?.description)
            }
        }
    }

    private val boltCreateV1 = with(MgrTaskStub.get()) {
        TaskCreateRequest(
            task = TaskCreateObject(
                title = title,
                description = description
            ),
            requestType = "create",
            debug = TaskDebug(
                mode = TaskRequestDebugMode.STUB,
                stub = TaskRequestDebugStubs.SUCCESS
            )
        )
    }

    private val boltCreateV2 = with(MgrTaskStub.get()) {
        TaskCreateRequestV2(
            task = TaskCreateObjectV2(
                title = title,
                description = description
            ),
            debug = TaskDebugV2(
                mode = TaskRequestDebugModeV2.STUB,
                stub = TaskRequestDebugStubsV2.SUCCESS
            )
        )
    }
}

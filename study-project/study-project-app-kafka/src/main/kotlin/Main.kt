package ru.demyanovaf.kotlin.taskManager.app.kafka

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config, listOf(ConsumerStrategyV1(), ConsumerStrategyV2()))
    consumer.start()
}

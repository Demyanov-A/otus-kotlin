package ru.demyanovaf.kotlin.taskManager.app.kafka

import ru.demyanovaf.kotlin.taskManager.app.common.IMgrAppSettings
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.logging.common.TmLoggerProvider
import ru.demyanovaf.kotlin.taskManager.logging.jvm.tmLoggerLogback

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    val kafkaTopicInV2: String = KAFKA_TOPIC_IN_V2,
    val kafkaTopicOutV2: String = KAFKA_TOPIC_OUT_V2,
    override val corSettings: MgrCorSettings = MgrCorSettings(
        loggerProvider = TmLoggerProvider { tmLoggerLogback(it) }
    ),
    override val processor: MgrTaskProcessor = MgrTaskProcessor(corSettings),
) : IMgrAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_TOPIC_IN_V2_VAR = "KAFKA_TOPIC_IN_V2"
        const val KAFKA_TOPIC_OUT_V2_VAR = "KAFKA_TOPIC_OUT_V2"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,; ]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "taskManager" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "taskManager-task-v1-in" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "taskManager-task-v1-out" }
        val KAFKA_TOPIC_IN_V2 by lazy { System.getenv(KAFKA_TOPIC_IN_V2_VAR) ?: "taskManager-task-v2-in" }
        val KAFKA_TOPIC_OUT_V2 by lazy { System.getenv(KAFKA_TOPIC_OUT_V2_VAR) ?: "taskManager-task-v2-out" }
    }
}

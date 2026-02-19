package ru.demyanovaf.kotlin.taskManager.e2e.be

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import ru.demyanovaf.kotlin.taskManager.blackbox.fixture.client.KafkaClient
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDebug as TaskDebugV1
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskRequestDebugMode as TaskRequestDebugModeV1
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug as TaskDebugV2
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugMode as TaskRequestDebugModeV2
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.BaseContainerTest
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.client.Client
import ru.demyanovaf.kotlin.taskManager.e2e.be.docker.KafkaDockerCompose
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1.ScenariosV1
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v2.ScenariosV2

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestKafka : BaseContainerTest(KafkaDockerCompose){
    private val client: Client = KafkaClient(compose)
    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class V1 : ScenariosV1(client, TaskDebugV1(mode = TaskRequestDebugModeV1.PROD))
    @Nested
    internal inner class V2 : ScenariosV2(client, TaskDebugV2(mode = TaskRequestDebugModeV2.PROD))
}
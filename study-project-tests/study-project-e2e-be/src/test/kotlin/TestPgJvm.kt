package ru.demyanovaf.kotlin.taskManager.e2e.be

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskDebug as TaskDebugV1
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskRequestDebugMode as TaskRequestDebugModeV1
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.BaseContainerTest
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.client.Client
import ru.demyanovaf.kotlin.taskManager.e2e.be.base.client.RestClient
import ru.demyanovaf.kotlin.taskManager.e2e.be.docker.SpringDockerCompose
import ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1.ScenariosV1

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestPgJvm: BaseContainerTest(SpringDockerCompose) {
    private val client: Client = RestClient(compose)
    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class V1: ScenariosV1(client, TaskDebugV1(mode = TaskRequestDebugModeV1.PROD))
/*    @Nested
    internal inner class V2: ScenariosV2(client, TaskDebugV2(mode = TaskRequestDebugModeV2.PROD))*/
}
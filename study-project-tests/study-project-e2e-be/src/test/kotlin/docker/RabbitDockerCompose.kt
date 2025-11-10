package ru.demyanovaf.kotlin.taskManager.e2e.be.docker

import ru.demyanovaf.kotlin.taskManager.e2e.be.base.AbstractDockerCompose

object RabbitDockerCompose : AbstractDockerCompose(
    "rabbit_1", 5672, "docker-compose-rabbit.yml"
) {
    override val user: String
        get() = "guest"
    override val password: String
        get() = "guest"
}

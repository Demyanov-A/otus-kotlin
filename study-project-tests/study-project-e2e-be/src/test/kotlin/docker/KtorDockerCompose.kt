package ru.demyanovaf.kotlin.taskManager.e2e.be.docker

import ru.demyanovaf.kotlin.taskManager.e2e.be.base.AbstractDockerCompose

object KtorDockerCompose : AbstractDockerCompose(
    "app-ktor_1", 8080, "docker-compose-ktor.yml"
)

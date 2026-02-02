package ru.demyanovaf.kotlin.taskManager.e2e.be.docker

import ru.demyanovaf.kotlin.taskManager.e2e.be.base.AbstractDockerCompose

object SpringDockerCompose : AbstractDockerCompose(
    "app-spring_1", 8080, "docker-compose-spring-pg.yml"
)

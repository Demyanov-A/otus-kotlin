package ru.demyanovaf.kotlin.taskManager.e2e.be.docker

import ru.demyanovaf.kotlin.taskManager.e2e.be.base.AbstractDockerCompose

object KafkaDockerCompose : AbstractDockerCompose(
    "kafka_1", 9091, "docker-compose-kafka.yml"
)

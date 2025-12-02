package ru.demyanovaf.kotlin.taskManager.app.rabbit.config

import ru.demyanovaf.kotlin.taskManager.app.common.IMgrAppSettings

interface IMgrAppRabbitSettings: IMgrAppSettings {
    val rabbit: RabbitConfig
    val controllersConfigV1: RabbitExchangeConfiguration
    val controllersConfigV2: RabbitExchangeConfiguration
}

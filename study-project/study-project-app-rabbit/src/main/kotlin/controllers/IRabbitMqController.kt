package ru.demyanovaf.kotlin.taskManager.app.rabbit.controllers

import ru.demyanovaf.kotlin.taskManager.app.rabbit.config.RabbitExchangeConfiguration

interface IRabbitMqController {
    val exchangeConfig: RabbitExchangeConfiguration
    suspend fun process()
    fun close()
}


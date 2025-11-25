package ru.demyanovaf.kotlin.taskManager.app.rabbit.config

import ru.demyanovaf.kotlin.taskManager.app.common.IMgrAppSettings
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings

data class MgrAppSettings(
    override val corSettings: MgrCorSettings = MgrCorSettings(),
    override val processor: MgrTaskProcessor = MgrTaskProcessor(corSettings),
    override val rabbit: RabbitConfig = RabbitConfig(),
    override val controllersConfigV1: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
    override val controllersConfigV2: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
): IMgrAppSettings, IMgrAppRabbitSettings

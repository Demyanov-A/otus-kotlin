package ru.demyanovaf.kotlin.taskManager.app.spring.base

import ru.demyanovaf.kotlin.taskManager.app.common.IMgrAppSettings
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings

data class MgrAppSettings(
    override val corSettings: MgrCorSettings,
    override val processor: MgrTaskProcessor,
) : IMgrAppSettings
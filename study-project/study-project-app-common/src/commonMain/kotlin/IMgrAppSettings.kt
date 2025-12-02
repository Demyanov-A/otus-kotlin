package ru.demyanovaf.kotlin.taskManager.app.common

import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings

interface IMgrAppSettings {
    val processor: MgrTaskProcessor
    val corSettings: MgrCorSettings
}

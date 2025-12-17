package ru.demyanovaf.kotlin.taskManager.biz.validation

import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand

abstract class BaseBizValidationTest {
    protected abstract val command: MgrCommand
    private val settings by lazy { MgrCorSettings() }
    protected val processor by lazy { MgrTaskProcessor(settings) }
}

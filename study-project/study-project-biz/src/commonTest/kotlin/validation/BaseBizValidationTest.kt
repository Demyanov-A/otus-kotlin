package ru.demyanovaf.kotlin.taskManager.biz.validation

import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.repo.common.TaskRepoInitialized
import ru.demyanovaf.kotlin.taskManager.repo.inmemory.TaskRepoInMemory
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub

abstract class BaseBizValidationTest {
    protected abstract val command: MgrCommand
    private val repo = TaskRepoInitialized(
        repo = TaskRepoInMemory(),
        initObjects = listOf(
            MgrTaskStub.get(),
        ),
    )
    private val settings by lazy { MgrCorSettings(repoTest = repo) }
    protected val processor by lazy { MgrTaskProcessor(settings) }
}

package ru.demyanovaf.kotlin.taskManager.biz.repo

import ru.demyanovaf.kotlin.taskManager.biz.exceptions.MgrTaskDbNotConfiguredException
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.helpers.errorSystem
import ru.demyanovaf.kotlin.taskManager.common.helpers.fail
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker

fun ICorChainDsl<MgrContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        taskRepo = when (workMode) {
            MgrWorkMode.TEST -> corSettings.repoTest
            MgrWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != MgrWorkMode.STUB && taskRepo == IRepoTask.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = MgrTaskDbNotConfiguredException(workMode)
            )
        )
    }
}

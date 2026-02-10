package ru.demyanovaf.kotlin.taskManager.common

import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import ru.demyanovaf.kotlin.taskManager.common.ws.IMgrWsSessionRepo
import ru.demyanovaf.kotlin.taskManager.logging.common.TmLoggerProvider

data class MgrCorSettings(
    val loggerProvider: TmLoggerProvider = TmLoggerProvider(),
    val wsSessions: IMgrWsSessionRepo = IMgrWsSessionRepo.NONE,
    val repoStub: IRepoTask = IRepoTask.NONE,
    val repoTest: IRepoTask = IRepoTask.NONE,
    val repoProd: IRepoTask = IRepoTask.NONE,
) {
    companion object {
        val NONE = MgrCorSettings()
    }
}

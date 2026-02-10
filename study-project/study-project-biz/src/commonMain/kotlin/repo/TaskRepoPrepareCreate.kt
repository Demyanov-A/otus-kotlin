package ru.demyanovaf.kotlin.taskManager.biz.repo

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker

fun ICorChainDsl<MgrContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == MgrState.RUNNING }
    handle {
        taskRepoPrepare = taskValidated.deepCopy()
        // TODO будет реализовано в занятии по управлению пользвателями
        //taskRepoPrepare.userId = MgrUserId.NONE
    }
}

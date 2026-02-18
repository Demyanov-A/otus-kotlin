package ru.demyanovaf.kotlin.taskManager.biz.repo

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker

fun ICorChainDsl<MgrContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == MgrState.RUNNING }
    handle {
        taskRepoPrepare = taskRepoRead.deepCopy().apply {
            this.userId = taskValidated.userId
            this.title = taskValidated.title
            this.description = taskValidated.description
            this.status = taskValidated.status
            this.category = taskValidated.category
            this.deadline = taskValidated.deadline
            this.lock = taskValidated.lock
        }
    }
}

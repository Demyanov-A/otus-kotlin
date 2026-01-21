package ru.demyanovaf.kotlin.taskManager.biz.validation

import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.helpers.errorValidation
import ru.demyanovaf.kotlin.taskManager.common.helpers.fail
import ru.demyanovaf.kotlin.taskManager.cor.ICorChainDsl
import ru.demyanovaf.kotlin.taskManager.cor.worker

// смотрим пример COR DSL валидации
fun ICorChainDsl<MgrContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { taskValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

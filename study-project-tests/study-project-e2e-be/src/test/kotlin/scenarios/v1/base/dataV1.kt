package ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v1.base

import ru.demyanovaf.kotlin.taskManager.api.v1.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v1.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v1.models.TaskCreateObject

val someCreateTask = TaskCreateObject(
    title = "Задача №1",
    description = "Задача №1",
    status = Status.NEW,
    category = Category.LOW
)

package ru.demyanovaf.kotlin.taskManager.e2e.be.scenarios.v2.base

import ru.demyanovaf.kotlin.taskManager.api.v2.models.Category
import ru.demyanovaf.kotlin.taskManager.api.v2.models.Status
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskCreateObject
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskDebug
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugMode
import ru.demyanovaf.kotlin.taskManager.api.v2.models.TaskRequestDebugStubs


val debug = TaskDebug(mode = TaskRequestDebugMode.STUB, stub = TaskRequestDebugStubs.SUCCESS)

val someCreateTask = TaskCreateObject(
    title = "Задача №1",
    description = "Задача №1",
    status = Status.NEW,
    category = Category.LOW
)

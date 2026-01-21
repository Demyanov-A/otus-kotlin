package ru.demyanovaf.kotlin.taskManager.biz.validation

import kotlinx.coroutines.test.runTest
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
    val ctx = MgrContext(
        command = command,
        state = MgrState.NONE,
        workMode = MgrWorkMode.TEST,
        taskRequest = MgrTask(
            id = MgrTaskId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            status = MgrStatus.NEW,
            category = MgrCategory.LOW,
            lock = MgrTaskLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MgrState.FAILING, ctx.state)
}

fun validationIdTrim(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
    val ctx = MgrContext(
        command = command,
        state = MgrState.NONE,
        workMode = MgrWorkMode.TEST,
        taskRequest = MgrTask(
            id = MgrTaskId(" \n\t 123-234-abc-ABC \n\t "),
            title = "abc",
            description = "abc",
            status = MgrStatus.NEW,
            category = MgrCategory.LOW,
            lock = MgrTaskLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MgrState.FAILING, ctx.state)
}

fun validationIdEmpty(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
    val ctx = MgrContext(
        command = command,
        state = MgrState.NONE,
        workMode = MgrWorkMode.TEST,
        taskRequest = MgrTask(
            id = MgrTaskId(""),
            title = "abc",
            description = "abc",
            status = MgrStatus.NEW,
            category = MgrCategory.LOW,
            lock = MgrTaskLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MgrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
    val ctx = MgrContext(
        command = command,
        state = MgrState.NONE,
        workMode = MgrWorkMode.TEST,
        taskRequest = MgrTask(
            id = MgrTaskId("!@#\$%^&*(),.{}"),
            title = "abc",
            description = "abc",
            status = MgrStatus.NEW,
            category = MgrCategory.LOW,
            lock = MgrTaskLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MgrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

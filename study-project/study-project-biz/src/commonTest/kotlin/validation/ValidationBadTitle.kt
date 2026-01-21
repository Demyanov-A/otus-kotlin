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
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MgrTaskStub.get()

fun validationTitleCorrect(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
    val ctx = MgrContext(
        command = command,
        state = MgrState.NONE,
        workMode = MgrWorkMode.TEST,
        taskRequest = MgrTask(
            id = stub.id,
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
    assertEquals("abc", ctx.taskValidated.title)
}

fun validationTitleTrim(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
    val ctx = MgrContext(
        command = command,
        state = MgrState.NONE,
        workMode = MgrWorkMode.TEST,
        taskRequest = MgrTask(
            id = stub.id,
            title = " \n\t abc \t\n ",
            description = "abc",
            status = MgrStatus.NEW,
            category = MgrCategory.LOW,
            lock = MgrTaskLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MgrState.FAILING, ctx.state)
    assertEquals("abc", ctx.taskValidated.title)
}

fun validationTitleEmpty(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
    val ctx = MgrContext(
        command = command,
        state = MgrState.NONE,
        workMode = MgrWorkMode.TEST,
        taskRequest = MgrTask(
            id = stub.id,
            title = "",
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
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

fun validationTitleSymbols(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
    val ctx = MgrContext(
        command = command,
        state = MgrState.NONE,
        workMode = MgrWorkMode.TEST,
        taskRequest = MgrTask(
            id = MgrTaskId("123"),
            title = "!@#$%^&*(),.{}",
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
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

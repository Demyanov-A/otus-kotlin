package ru.demyanovaf.kotlin.taskManager.biz.validation

import kotlinx.coroutines.test.runTest
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
    val ctx = MgrContext(
        command = command,
        state = MgrState.NONE,
        workMode = MgrWorkMode.TEST,
        taskRequest = MgrTaskStub.get()
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
        taskRequest = MgrTaskStub.prepareResult {
            id = MgrTaskId(" \n\t ${id.asString()} \n\t ")
        },
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
        taskRequest = MgrTaskStub.prepareResult {
            id = MgrTaskId("")
        },
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
        taskRequest = MgrTaskStub.prepareResult {
            id = MgrTaskId("!@#\$%^&*(),.{}")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MgrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

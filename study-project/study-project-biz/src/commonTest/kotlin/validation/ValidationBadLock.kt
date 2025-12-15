package validation

import kotlinx.coroutines.test.runTest
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
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

fun validationLockTrim(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
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
            lock = MgrTaskLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MgrState.FAILING, ctx.state)
}

fun validationLockEmpty(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
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
            lock = MgrTaskLock(""),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MgrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: MgrCommand, processor: MgrTaskProcessor) = runTest {
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
            lock = MgrTaskLock("!@#\$%^&*(),.{}"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MgrState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

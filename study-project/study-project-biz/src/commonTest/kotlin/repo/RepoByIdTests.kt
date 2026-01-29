package repo

import kotlinx.coroutines.test.runTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.TaskRepositoryMock
import ru.demyanovaf.kotlin.taskManager.biz.MgrTaskProcessor
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskResponseOk
import ru.demyanovaf.kotlin.taskManager.common.repo.errorNotFound
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initTask = MgrTask(
    id = MgrTaskId("123"),
    title = "abc",
    description = "abc",
    status = MgrStatus.NEW,
    category = MgrCategory.LOW,
)
private val repo = TaskRepositoryMock(
        invokeReadTask = {
            if (it.id == initTask.id) {
                DbTaskResponseOk(
                    data = initTask,
                )
            } else errorNotFound(it.id)
        }
    )
private val settings = MgrCorSettings(repoTest = repo)
private val processor = MgrTaskProcessor(settings)

fun repoNotFoundTest(command: MgrCommand) = runTest {
    val ctx = MgrContext(
        command = command,
        state = MgrState.NONE,
        workMode = MgrWorkMode.TEST,
        taskRequest = MgrTask(
            id = MgrTaskId("12345"),
            title = "xyz",
            description = "xyz",
            status = MgrStatus.NEW,
            category = MgrCategory.LOW,
            lock = MgrTaskLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(MgrState.FAILING, ctx.state)
    assertEquals(MgrTask(), ctx.taskResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}

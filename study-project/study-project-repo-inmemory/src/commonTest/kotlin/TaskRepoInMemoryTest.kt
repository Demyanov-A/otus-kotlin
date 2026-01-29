import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.RepoTaskCreateTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.RepoTaskDeleteTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.RepoTaskReadTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.RepoTaskSearchTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.RepoTaskUpdateTest
import ru.demyanovaf.kotlin.taskManager.repo.common.TaskRepoInitialized
import ru.demyanovaf.kotlin.taskManager.repo.inmemory.TaskRepoInMemory

class TaskRepoInMemoryCreateTest : RepoTaskCreateTest() {
    override val repo = TaskRepoInitialized(
        TaskRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class TaskRepoInMemoryDeleteTest : RepoTaskDeleteTest() {
    override val repo = TaskRepoInitialized(
        TaskRepoInMemory(),
        initObjects = initObjects,
    )
}

class TaskRepoInMemoryReadTest : RepoTaskReadTest() {
    override val repo = TaskRepoInitialized(
        TaskRepoInMemory(),
        initObjects = initObjects,
    )
}

class TaskRepoInMemorySearchTest : RepoTaskSearchTest() {
    override val repo = TaskRepoInitialized(
        TaskRepoInMemory(),
        initObjects = initObjects,
    )
}

class TaskRepoInMemoryUpdateTest : RepoTaskUpdateTest() {
    override val repo = TaskRepoInitialized(
        TaskRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}

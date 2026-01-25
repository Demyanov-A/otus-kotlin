package ru.demyanovaf.kotlin.taskManager.app.spring.repo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import kotlinx.datetime.Instant
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import ru.demyanovaf.kotlin.taskManager.app.spring.config.TaskConfig
import ru.demyanovaf.kotlin.taskManager.app.spring.controllers.TaskControllerV1Fine
import ru.demyanovaf.kotlin.taskManager.common.NONE
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCategory
import ru.demyanovaf.kotlin.taskManager.common.models.MgrStatus
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskFilterRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskIdRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.DbTaskRequest
import ru.demyanovaf.kotlin.taskManager.common.repo.IRepoTask
import ru.demyanovaf.kotlin.taskManager.repo.common.TaskRepoInitialized
import ru.demyanovaf.kotlin.taskManager.repo.inmemory.TaskRepoInMemory
import ru.demyanovaf.kotlin.taskManager.stubs.MgrTaskStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(TaskControllerV1Fine::class, TaskConfig::class)
internal class TaskRepoInMemoryV1Test : TaskRepoBaseV1Test() {
    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoTask

    @BeforeEach
    fun tearUp() {
        val slotTask = slot<DbTaskRequest>()
        val slotId = slot<DbTaskIdRequest>()
        val slotFl = slot<DbTaskFilterRequest>()
        val repo = TaskRepoInitialized(
            repo = TaskRepoInMemory(randomUuid = { uuidNew }),
            initObjects = MgrTaskStub.prepareSearchList("xx", MgrStatus.NEW, MgrCategory.LOW, Instant.NONE) + MgrTaskStub.get()
        )
        coEvery { testTestRepo.createTask(capture(slotTask)) } coAnswers { repo.createTask(slotTask.captured) }
        coEvery { testTestRepo.readTask(capture(slotId)) } coAnswers { repo.readTask(slotId.captured) }
        coEvery { testTestRepo.updateTask(capture(slotTask)) } coAnswers { repo.updateTask(slotTask.captured) }
        coEvery { testTestRepo.deleteTask(capture(slotId)) } coAnswers { repo.deleteTask(slotId.captured) }
        coEvery { testTestRepo.searchTask(capture(slotFl)) } coAnswers { repo.searchTask(slotFl.captured) }
    }

    @Test
    override fun createTask() = super.createTask()
    @Test
    override fun readTask() = super.readTask()
    @Test
    override fun updateTask() = super.updateTask()
    @Test
    override fun deleteTask() = super.deleteTask()
    @Test
    override fun searchTask() = super.searchTask()
}

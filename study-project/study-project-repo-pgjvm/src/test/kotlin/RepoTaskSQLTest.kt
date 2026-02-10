package ru.demyanovaf.kotlin.taskManager.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.RepoTaskCreateTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.RepoTaskDeleteTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.RepoTaskReadTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.RepoTaskSearchTest
import ru.demyanovaf.kotlin.taskManager.backend.repo.tests.RepoTaskUpdateTest
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.repo.common.IRepoTaskInitializable
import ru.demyanovaf.kotlin.taskManager.repo.common.TaskRepoInitialized
import java.io.File
import java.time.Duration
import kotlin.test.AfterTest
import kotlin.test.Ignore


private fun IRepoTaskInitializable.clear() {
    val pgRepo = (this as TaskRepoInitialized).repo as RepoTaskSql
    pgRepo.clear()
}

@RunWith(Enclosed::class)
class RepoTaskSQLTest {

    class RepoTaskSQLCreateTest : RepoTaskCreateTest() {
        override val repo = repoUnderTestContainer(
            initObjects,
            randomUuid = { uuidNew.asString() },
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoTaskSQLReadTest : RepoTaskReadTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoTaskSQLUpdateTest : RepoTaskUpdateTest() {
        override val repo = repoUnderTestContainer(
            initObjects,
            randomUuid = { lockNew.asString() },
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoTaskSQLDeleteTest : RepoTaskDeleteTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoTaskSQLSearchTest : RepoTaskSearchTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    @Ignore
    companion object {
        private const val PG_SERVICE = "psql"
        private const val MG_SERVICE = "liquibase"

        // val LOGGER = org.slf4j.LoggerFactory.getLogger(ComposeContainer::class.java)
        private val container: ComposeContainer by lazy {
            val res = this::class.java.classLoader.getResource("docker-compose-pg.yml")
                ?: throw Exception("No resource found")
            val file = File(res.toURI())
            //  val logConsumer = Slf4jLogConsumer(LOGGER)
            ComposeContainer(
                file,
            )
                .withExposedService(PG_SERVICE, 5432)
                .withStartupTimeout(Duration.ofSeconds(300))
//                .withLogConsumer(MG_SERVICE, logConsumer)
//                .withLogConsumer(PG_SERVICE, logConsumer)
                .waitingFor(
                    MG_SERVICE,
                    Wait.forLogMessage(".*Liquibase command 'update' was executed successfully.*", 1)
                )
        }

        private const val HOST = "localhost"
        private const val USER = "postgres"
        private const val PASS = "taskManager-pass"
        private val PORT by lazy {
            container.getServicePort(PG_SERVICE, 5432) ?: 5432
        }

        fun repoUnderTestContainer(
            initObjects: Collection<MgrTask> = emptyList(),
            randomUuid: () -> String = { uuid4().toString() },
        ): IRepoTaskInitializable = TaskRepoInitialized(
            repo = RepoTaskSql(
                SqlProperties(
                    host = HOST,
                    user = USER,
                    password = PASS,
                    port = PORT,
                ),
                randomUuid = randomUuid
            ),
            initObjects = initObjects,
        )

        @JvmStatic
        @BeforeClass
        fun start() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun finish() {
            container.stop()
        }
    }
}


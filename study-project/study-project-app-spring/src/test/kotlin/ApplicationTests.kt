package ru.demyanovaf.kotlin.taskManager.app.spring

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.demyanovaf.kotlin.taskManager.app.spring.config.TaskConfigPostgres

@SpringBootTest
class ApplicationTests {
    @Autowired
    var pgConf: TaskConfigPostgres = TaskConfigPostgres()

    @Test
    fun contextLoads() {
        assertEquals(5432, pgConf.psql.port)
        assertEquals("test_db", pgConf.psql.database)
    }
}

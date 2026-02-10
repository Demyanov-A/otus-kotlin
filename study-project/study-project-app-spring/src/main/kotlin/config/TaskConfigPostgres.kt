package ru.demyanovaf.kotlin.taskManager.app.spring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.demyanovaf.kotlin.taskManager.backend.repo.postgresql.SqlProperties

// Так не работает
//@ConfigurationProperties(prefix = "taskManager.repository.psql")
@ConfigurationProperties(prefix = "psql")
data class TaskConfigPostgres(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "taskManager-pass",
    var database: String = "taskManager",
    var schema: String = "public",
    var table: String = "tasks",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}

package ru.demyanovaf.kotlin.taskManager.backend.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "taskManager-pass",
    val database: String = "taskManager",
    val schema: String = "public",
    val table: String = "tasks",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}

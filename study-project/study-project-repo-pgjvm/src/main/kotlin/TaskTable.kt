package ru.demyanovaf.kotlin.taskManager.backend.repo.postgresql

import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTask
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.common.models.MgrUserId

class TaskTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val description = text(SqlFields.DESCRIPTION).nullable()
    val user = text(SqlFields.USER_ID)
    val category = categoryEnumeration(SqlFields.CATEGORY)
    val status = taskTypeEnumeration(SqlFields.STATUS)
    val dtCreated = text(SqlFields.DT_CREATE)
    val deadline = text(SqlFields.DEADLINE)
    val lock = text(SqlFields.LOCK)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = MgrTask(
        id = MgrTaskId(res[id]),
        title = res[title] ?: "",
        description = res[description] ?: "",
        userId = MgrUserId(res[user]),
        category = res[category],
        status = res[status],
        deadline = Instant.parse(res[deadline]),
        dtCreate = Instant.parse(res[dtCreated]),
        lock = MgrTaskLock(res[lock]),
    )

    fun UpdateBuilder<*>.to(task: MgrTask, randomUuid: () -> String) {
        this[id] = task.id.takeIf { it != MgrTaskId.NONE }?.asString() ?: randomUuid()
        this[title] = task.title
        this[description] = task.description
        this[user] = task.userId.asString()
        this[category] = task.category
        this[status] = task.status
        this[dtCreated] = task.dtCreate.toString()
        this[deadline] = task.deadline.toString()
        this[lock] = task.lock.takeIf { it != MgrTaskLock.NONE }?.asString() ?: randomUuid()
    }
}


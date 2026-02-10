package ru.demyanovaf.kotlin.taskManager.backend.repo.postgresql

object SqlFields {
    const val ID = "id"
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val STATUS = "status"
    const val CATEGORY = "category"
    const val LOCK = "lock"
    const val DT_CREATE = "dtCreate"
    const val DEADLINE = "deadline"
    const val LOCK_OLD = "lock_old"
    const val USER_ID = "user_id"

    const val TASK_STATUS_TYPE = "task_status_type"
    const val TASK_STATUS_NEW = "new"
    const val TASK_STATUS_TO_DO = "toDo"
    const val TASK_STATUS_IN_PROGRESS = "inProgress"
    const val TASK_STATUS_CANCELED = "canceled"
    const val TASK_STATUS_HOLD = "hold"
    const val TASK_STATUS_DONE = "done"

    const val CATEGORY_TYPE = "task_category_type"
    const val CATEGORY_LOW = "low"
    const val CATEGORY_MEDIUM = "medium"
    const val CATEGORY_HI = "hi"
    const val CATEGORY_PERSONAL = "personal"

    const val FILTER_TITLE = TITLE
    const val FILTER_USER_ID = USER_ID
    const val FILTER_STATUS_TYPE = STATUS
    const val FILTER_CATEGORY_TYPE = CATEGORY_TYPE
    const val FILTER_DT_CREATE = DT_CREATE
    const val FILTER_DEADLINE = DEADLINE

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, TITLE, DESCRIPTION, STATUS, CATEGORY, LOCK, USER_ID, DT_CREATE, DEADLINE
    )
}

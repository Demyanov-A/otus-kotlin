package ru.demyanovaf.kotlin.taskManager.common.repo.exceptions

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId

open class RepoTaskException(
    @Suppress("unused")
    val adId: MgrTaskId,
    msg: String,
): RepoException(msg)

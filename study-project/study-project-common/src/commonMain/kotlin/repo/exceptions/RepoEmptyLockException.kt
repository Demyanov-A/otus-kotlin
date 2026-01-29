package ru.demyanovaf.kotlin.taskManager.common.repo.exceptions

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId

class RepoEmptyLockException(id: MgrTaskId) : RepoTaskException(
    id,
    "Lock is empty in DB"
)
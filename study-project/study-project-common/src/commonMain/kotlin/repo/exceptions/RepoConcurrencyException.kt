package ru.demyanovaf.kotlin.taskManager.common.repo.exceptions

import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock

class RepoConcurrencyException(id: MgrTaskId, expectedLock: MgrTaskLock, actualLock: MgrTaskLock?) : RepoTaskException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
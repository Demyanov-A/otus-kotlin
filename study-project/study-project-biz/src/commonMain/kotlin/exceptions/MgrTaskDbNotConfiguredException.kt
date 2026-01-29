package ru.demyanovaf.kotlin.taskManager.biz.exceptions

import ru.demyanovaf.kotlin.taskManager.common.models.MgrWorkMode

class MgrTaskDbNotConfiguredException(val workMode: MgrWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)

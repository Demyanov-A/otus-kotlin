package ru.demyanovaf.kotlin.taskManager.common.models

import kotlinx.datetime.Instant
import ru.demyanovaf.kotlin.taskManager.common.NONE

data class MgrTaskFilter(
    var searchString: String = "",
    var userId: MgrUserId = MgrUserId.NONE,
    var status: MgrStatus = MgrStatus.NONE,
    var deadline: Instant = Instant.NONE,
    var dtCreate: Instant = Instant.NONE,
){
    fun deepCopy(): MgrTaskFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MgrTaskFilter()
    }
}

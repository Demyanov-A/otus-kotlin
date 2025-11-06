package ru.demyanovaf.kotlin.taskManager.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MgrTaskLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MgrTaskLock("")
    }
}

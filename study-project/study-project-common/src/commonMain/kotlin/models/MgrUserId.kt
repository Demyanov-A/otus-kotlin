package ru.demyanovaf.kotlin.taskManager.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MgrUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MgrUserId("")
    }
}

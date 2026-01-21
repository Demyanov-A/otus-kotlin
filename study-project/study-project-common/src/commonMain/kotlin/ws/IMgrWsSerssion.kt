package ru.demyanovaf.kotlin.taskManager.common.ws

interface IMgrWsSession {
    suspend fun <T> send(obj: T)

    companion object {
        val NONE = object : IMgrWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}

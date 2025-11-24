package ru.demyanovaf.kotlin.taskManager.common.ws

interface IMgrWsSessionRepo {
    fun add(session: IMgrWsSession)
    fun clearAll()
    fun remove(session: IMgrWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IMgrWsSessionRepo {
            override fun add(session: IMgrWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IMgrWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}

package ru.demyanovaf.kotlin.taskManager.app.spring.base

import ru.demyanovaf.kotlin.taskManager.common.ws.IMgrWsSession
import ru.demyanovaf.kotlin.taskManager.common.ws.IMgrWsSessionRepo

class SpringWsSessionRepo : IMgrWsSessionRepo {
    private val sessions: MutableSet<IMgrWsSession> = mutableSetOf()
    override fun add(session: IMgrWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IMgrWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}

package ru.demyanovaf.kotlin.taskManager.biz.validation

import kotlinx.coroutines.test.runTest
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskFilter
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MgrContext(state = MgrState.RUNNING, taskFilterValidating = MgrTaskFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(MgrState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = MgrContext(state = MgrState.RUNNING, taskFilterValidating = MgrTaskFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(MgrState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = MgrContext(state = MgrState.RUNNING, taskFilterValidating = MgrTaskFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(MgrState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MgrContext(state = MgrState.RUNNING, taskFilterValidating = MgrTaskFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(MgrState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = MgrContext(state = MgrState.RUNNING, taskFilterValidating = MgrTaskFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(MgrState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}

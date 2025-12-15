package ru.demyanovaf.kotlin.taskManager.api.v2.mappers

import ru.demyanovaf.kotlin.taskManager.api.v2.models.*
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.models.*
import ru.demyanovaf.kotlin.taskManager.common.stubs.MgrStubs

// Демонстрация форматной валидации в мапере
private sealed interface Result<T,E>
private data class Ok<T,E>(val value: T) : Result<T,E>
private data class Err<T,E>(val errors: List<E>) : Result<T,E> {
    constructor(error: E) : this(listOf(error))
}

private fun <T,E> Result<T,E>.getOrExec(default: T, block: (Err<T,E>) -> Unit = {}): T = when (this) {
    is Ok<T,E> -> this.value
    is Err<T,E> -> {
        block(this)
        default
    }
}

@Suppress("unused")
private fun <T,E> Result<T,E>.getOrNull(block: (Err<T,E>) -> Unit = {}): T? = when (this) {
    is Ok<T,E> -> this.value
    is Err<T,E> -> {
        block(this)
        null
    }
}

private fun String?.transportToStubCaseValidated(): Result<MgrStubs,MgrError> = when (this) {
    "success" -> Ok(MgrStubs.SUCCESS)
    "notFound" -> Ok(MgrStubs.NOT_FOUND)
    "badId" -> Ok(MgrStubs.BAD_ID)
    "badTitle" -> Ok(MgrStubs.BAD_TITLE)
    "badDescription" -> Ok(MgrStubs.BAD_DESCRIPTION)
    "badStatus" -> Ok(MgrStubs.BAD_STATUS)
    "badCategory" -> Ok(MgrStubs.BAD_CATEGORY)
    "cannotDelete" -> Ok(MgrStubs.CANNOT_DELETE)
    "badSearchString" -> Ok(MgrStubs.BAD_SEARCH_STRING)
    null -> Ok(MgrStubs.NONE)
    else -> Err(
        MgrError(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case \"$this\""
        )
    )
}

@Suppress("unused")
fun MgrContext.fromTransportValidated(request: TaskCreateRequest) {
    command = MgrCommand.CREATE
    // Вся магия здесь!
    stubCase = request
        .debug
        ?.stub
        ?.value
        .transportToStubCaseValidated()
        .getOrExec(MgrStubs.NONE) { err: Err<MgrStubs,MgrError> ->
            errors.addAll(err.errors)
            state = MgrState.FAILING
        }
}

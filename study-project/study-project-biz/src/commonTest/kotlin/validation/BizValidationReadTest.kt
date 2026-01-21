package ru.demyanovaf.kotlin.taskManager.biz.validation

import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import kotlin.test.Test

class BizValidationReadTest : BaseBizValidationTest() {
    override val command = MgrCommand.READ

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badFormatId() = validationIdFormat(command, processor)

}

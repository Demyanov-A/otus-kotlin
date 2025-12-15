package ru.demyanovaf.kotlin.taskManager.biz

import ru.demyanovaf.kotlin.taskManager.biz.general.initStatus
import ru.demyanovaf.kotlin.taskManager.biz.general.operation
import ru.demyanovaf.kotlin.taskManager.biz.general.stubs
import ru.demyanovaf.kotlin.taskManager.biz.stubs.stubCreateSuccess
import ru.demyanovaf.kotlin.taskManager.biz.stubs.stubDbError
import ru.demyanovaf.kotlin.taskManager.biz.stubs.stubDeleteSuccess
import ru.demyanovaf.kotlin.taskManager.biz.stubs.stubNoCase
import ru.demyanovaf.kotlin.taskManager.biz.stubs.stubReadSuccess
import ru.demyanovaf.kotlin.taskManager.biz.stubs.stubSearchSuccess
import ru.demyanovaf.kotlin.taskManager.biz.stubs.stubUpdateSuccess
import ru.demyanovaf.kotlin.taskManager.biz.stubs.stubValidationBadDescription
import ru.demyanovaf.kotlin.taskManager.biz.stubs.stubValidationBadId
import ru.demyanovaf.kotlin.taskManager.biz.stubs.stubValidationBadTitle
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.cor.rootChain

class MgrTaskProcessor(
    private val corSettings: MgrCorSettings = MgrCorSettings.NONE
) {
    suspend fun exec(ctx: MgrContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<MgrContext> {
        initStatus("Инициализация статуса")

        operation("Создание задачи", MgrCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Получить задачу", MgrCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Изменить задачу", MgrCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Удалить задачу", MgrCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Поиск задачи", MgrCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
    }.build()
}
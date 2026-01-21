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
import ru.demyanovaf.kotlin.taskManager.biz.validation.finishTaskFilterValidation
import ru.demyanovaf.kotlin.taskManager.biz.validation.finishTaskValidation
import ru.demyanovaf.kotlin.taskManager.biz.validation.validateDescriptionHasContent
import ru.demyanovaf.kotlin.taskManager.biz.validation.validateDescriptionNotEmpty
import ru.demyanovaf.kotlin.taskManager.biz.validation.validateIdNotEmpty
import ru.demyanovaf.kotlin.taskManager.biz.validation.validateIdProperFormat
import ru.demyanovaf.kotlin.taskManager.biz.validation.validateLockNotEmpty
import ru.demyanovaf.kotlin.taskManager.biz.validation.validateLockProperFormat
import ru.demyanovaf.kotlin.taskManager.biz.validation.validateSearchStringLength
import ru.demyanovaf.kotlin.taskManager.biz.validation.validateTitleHasContent
import ru.demyanovaf.kotlin.taskManager.biz.validation.validateTitleNotEmpty
import ru.demyanovaf.kotlin.taskManager.biz.validation.validation
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.MgrCorSettings
import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskId
import ru.demyanovaf.kotlin.taskManager.common.models.MgrTaskLock
import ru.demyanovaf.kotlin.taskManager.cor.rootChain
import ru.demyanovaf.kotlin.taskManager.cor.worker

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
            validation {
                worker("Копируем поля в taskValidating") { taskValidating = taskRequest.deepCopy() }
                worker("Очистка id") { taskValidating.id = MgrTaskId.NONE }
                worker("Очистка заголовка") { taskValidating.title = taskValidating.title.trim() }
                worker("Очистка описания") { taskValidating.description = taskValidating.description.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateTitleHasContent("Проверка символов")
                validateDescriptionNotEmpty("Проверка, что описание не пусто")
                validateDescriptionHasContent("Проверка символов")

                finishTaskValidation("Завершение проверок")
            }
        }
        operation("Получить задачу", MgrCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в taskValidating") { taskValidating = taskRequest.deepCopy() }
                worker("Очистка id") { taskValidating.id = MgrTaskId(taskValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishTaskValidation("Успешное завершение процедуры валидации")
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
            validation {
                worker("Копируем поля в taskValidating") { taskValidating = taskRequest.deepCopy() }
                worker("Очистка id") { taskValidating.id = MgrTaskId(taskValidating.id.asString().trim()) }
                worker("Очистка lock") { taskValidating.lock = MgrTaskLock(taskValidating.lock.asString().trim()) }
                worker("Очистка заголовка") { taskValidating.title = taskValidating.title.trim() }
                worker("Очистка описания") { taskValidating.description = taskValidating.description.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateTitleHasContent("Проверка на наличие содержания в заголовке")
                validateDescriptionNotEmpty("Проверка на непустое описание")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")

                finishTaskValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Удалить задачу", MgrCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в taskValidating") {
                    taskValidating = taskRequest.deepCopy()
                }
                worker("Очистка id") { taskValidating.id = MgrTaskId(taskValidating.id.asString().trim()) }
                worker("Очистка lock") { taskValidating.lock = MgrTaskLock(taskValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishTaskValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Поиск задач", MgrCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в taskFilterValidating") { taskFilterValidating = taskFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishTaskFilterValidation("Успешное завершение процедуры валидации")
            }
        }
    }.build()
}

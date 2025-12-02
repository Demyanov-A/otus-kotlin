package ru.demyanovaf.kotlin.taskManager.app.rabbit.controllers

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.demyanovaf.kotlin.taskManager.api.v1.apiV1Mapper
import ru.demyanovaf.kotlin.taskManager.api.v1.models.IRequest
import ru.demyanovaf.kotlin.taskManager.app.common.controllerHelper
import ru.demyanovaf.kotlin.taskManager.app.rabbit.config.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.helpers.asMgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState
import ru.demyanovaf.kotlin.taskManager.mappers.v1.fromTransport
import ru.demyanovaf.kotlin.taskManager.mappers.v1.toTransportTask

// наследник RabbitProcessorBase, увязывает транспортную и бизнес-части
class RabbitDirectControllerV1(
    private val appSettings: MgrAppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV1,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {
    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV1Mapper.readValue(message.body, IRequest::class.java)
                fromTransport(req)
            },
            {
                val res = toTransportTask()
                apiV1Mapper.writeValueAsBytes(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
                }
            },
            this@RabbitDirectControllerV1::class,
            "rabbitmq-v1-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = MgrContext()
        e.printStackTrace()
        context.state = MgrState.FAILING
        context.errors.add(e.asMgrError())
        val response = context.toTransportTask()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
        }
    }
}

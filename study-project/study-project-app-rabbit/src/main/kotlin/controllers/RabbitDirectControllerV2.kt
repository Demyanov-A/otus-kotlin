package ru.demyanovaf.kotlin.taskManager.app.rabbit.controllers

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2RequestDeserialize
import ru.demyanovaf.kotlin.taskManager.api.v2.apiV2ResponseSerialize
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.fromTransport
import ru.demyanovaf.kotlin.taskManager.api.v2.mappers.toTransportTask
import ru.demyanovaf.kotlin.taskManager.api.v2.models.IRequest
import ru.demyanovaf.kotlin.taskManager.app.common.controllerHelper
import ru.demyanovaf.kotlin.taskManager.app.rabbit.config.MgrAppSettings
import ru.demyanovaf.kotlin.taskManager.common.MgrContext
import ru.demyanovaf.kotlin.taskManager.common.helpers.asMgrError
import ru.demyanovaf.kotlin.taskManager.common.models.MgrState

class RabbitDirectControllerV2(
    private val appSettings: MgrAppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV2,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {

    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV2RequestDeserialize<IRequest>(String(message.body))
                fromTransport(req)
            },
            {
                val res = toTransportTask()
                apiV2ResponseSerialize(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it.toByteArray())
                }
            },
            RabbitDirectControllerV2::class,
            "rabbitmq-v2-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = MgrContext()
        e.printStackTrace()
        context.state = MgrState.FAILING
        context.errors.add(e.asMgrError())
        val response = context.toTransportTask()
        apiV2ResponseSerialize(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it.toByteArray())
        }
    }
}

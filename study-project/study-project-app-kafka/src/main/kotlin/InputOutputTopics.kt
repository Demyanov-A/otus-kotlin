package ru.demyanovaf.kotlin.taskManager.app.kafka

/**
 * Структура для хранения входящего и исходящего топиков для версии API
 */
data class InputOutputTopics(
    /**
     * Топик входящих в приложение сообщений
     */
    val input: String,
    /**
     * Топик для исходящих из приложения сообщений
     */
    val output: String,
)

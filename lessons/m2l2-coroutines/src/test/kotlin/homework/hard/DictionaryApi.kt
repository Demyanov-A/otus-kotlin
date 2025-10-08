package ru.otus.otuskotlin.coroutines.homework.hard

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response
import ru.otus.otuskotlin.coroutines.homework.hard.dto.Dictionary
import java.time.Clock

class DictionaryApi(
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
) {

    suspend fun findWord(locale: Locale, word: String): Dictionary? = withContext(Dispatchers.IO)  { // make something with context
        val url = "$DICTIONARY_API/${locale.code}/$word"
        println("Searching $url ${Clock.systemUTC().instant()}")
        Thread.sleep(1000)

        return@withContext getBody(HttpClient.get(url).execute())?.firstOrNull()
    }


    private fun getBody(response: Response): List<Dictionary>? {
        if (!response.isSuccessful) {
            return emptyList()
        }

        return response.body?.let {
            objectMapper.readValue(it.string())
        }
    }
}
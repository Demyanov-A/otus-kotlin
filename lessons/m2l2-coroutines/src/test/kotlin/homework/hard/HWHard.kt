package ru.otus.otuskotlin.coroutines.homework.hard

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.coroutines.homework.hard.dto.Dictionary
import java.io.File
import kotlin.test.Test

class HWHard {
    @Test
    fun hardHw(): Unit = runBlocking {
        val dictionaryApi = DictionaryApi()
        val words = FileReader.readFile().split(" ", "\n").toSet()

        val dictionaries = findWords(dictionaryApi, words, Locale.EN)

        dictionaries.filterNotNull().map { dictionary ->
            print("For word ${dictionary.word} i found examples: ")
            println(
                dictionary.meanings
                    .mapNotNull { definition ->
                        val r = definition.definitions
                            .mapNotNull { it.example.takeIf { it?.isNotBlank() == true } }
                            .takeIf { it.isNotEmpty() }
                        r
                    }
                    .takeIf { it.isNotEmpty() }
            )
        }
    }

    private suspend fun findWords(
        dictionaryApi: DictionaryApi,
        words: Set<String>,
        @Suppress("SameParameterValue") locale: Locale
    ): List<Dictionary?> = coroutineScope {
        // make some suspensions and async
        val out = mutableListOf<Deferred<Dictionary?>>()
        words.forEach {
            out.add(async{dictionaryApi.findWord(locale, it)})
        }
        return@coroutineScope out.awaitAll()
    }

    object FileReader {
        fun readFile(): String =
            File(
                this::class.java.classLoader.getResource("words.txt")?.toURI()
                    ?: throw RuntimeException("Can't read file")
            ).readText()
    }
}
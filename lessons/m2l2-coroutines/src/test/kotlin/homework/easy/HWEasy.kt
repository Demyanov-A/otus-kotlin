package ru.otus.otuskotlin.coroutines.homework.easy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.Clock

import kotlin.test.Test

class HWEasy {

    @Test
    fun easyHw(): Unit = runBlocking {
        val numbers = generateNumbers()
        val toFind = 10
        val toFindOther = 1000
        val foundNumbers = mutableListOf<Int>()

        suspend fun call1() = withContext(Dispatchers.Default) {
            println(Clock.systemUTC().instant())
            foundNumbers.add(findNumberInList(toFind, numbers))
            println("First coroutine")
        }

        suspend fun call2() = withContext(Dispatchers.Default)  {
            println(Clock.systemUTC().instant())
            foundNumbers.add(findNumberInList(toFindOther, numbers))
            println("Second coroutine")
        }

        coroutineScope {
            launch { call1() }
            launch { call2() }
        }

        foundNumbers.forEach {
            if (it != -1) {
                println("Your number $it found!")
            } else {
                println("Not found number $toFind || $toFindOther")
            }
        }
    }
}
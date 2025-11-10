package ru.demyanovaf.kotlin.taskManager.mappers.v1.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to MgrContext")

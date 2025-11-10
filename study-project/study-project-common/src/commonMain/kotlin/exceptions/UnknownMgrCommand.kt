package ru.demyanovaf.kotlin.taskManager.common.exceptions

import ru.demyanovaf.kotlin.taskManager.common.models.MgrCommand


class UnknownMgrCommand(command: MgrCommand) : Throwable("Wrong command $command at mapping toTransport stage")

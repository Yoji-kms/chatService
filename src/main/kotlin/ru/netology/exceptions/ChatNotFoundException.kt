package ru.netology.exceptions

import java.lang.RuntimeException

class ChatNotFoundException(message: String = "Chat not found exception") : RuntimeException(message)
package ru.netology

data class Message(
    val id: Long,
    val fromId: Long,
    val text: String = "text",
    val isRead: Boolean = false
)
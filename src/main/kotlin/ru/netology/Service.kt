package ru.netology

import ru.netology.exceptions.ChatNotFoundException
import ru.netology.exceptions.MessageNotFoundException

object Service {
    private val messages = mutableListOf<Message>()
    private val chats = mutableListOf<Chat>()

    fun clear() {
        messages.clear()
        chats.clear()
    }

    fun getRandomChatId(): Long {
        return chats.random().id
    }

    fun createMessage(fromId: Long, text: String, toIdsList: HashSet<Long>): Message {
        val message = Message(
            id = (messages.lastOrNull()?.id ?: -1) + 1,
            fromId = fromId,
            text = text,
            isRead = false
        )
        messages += message

        val userIdsList = (toIdsList + fromId) as HashSet<Long>
        val chat: Chat =
            chats
                .ifEmpty { createChat(userIdsList) }
                .let { chats.find { it.userIdsLst.containsAll(userIdsList) && it.userIdsLst.size == userIdsList.size }
                ?: createChat(userIdsList) }

        updateChatMessageList(chat = chat, message = message)

        return message
    }

    private fun createChat(userIdsList: HashSet<Long>): Chat {
        return Chat(
            id = (chats.lastOrNull()?.id ?: -1) + 1,
            userIdsLst = userIdsList,
            messageIdsList = emptyList(),
            unreadMessageIdsList = emptyList(),
            isRead = false
        )
    }

    fun editMessage(messageId: Long, newText: String): Message {
        val updatingMessage = messages.find { it.id == messageId } ?: throw MessageNotFoundException()

        return messages.replaceItem(
            replacingItem = updatingMessage,
            newItem = updatingMessage.copy(text = newText, isRead = false)
        )
    }

    private fun readChat(chat: Chat): Chat {
        messages.replaceAll { if (chat.messageIdsList.contains(it.id)) it.copy(isRead = true) else it }
        return chats.replaceItem(
            replacingItem = chat,
            newItem = chat.copy(isRead = true)
        )
    }

    fun readChatById(chatId: Long): Chat {
        return readChat(chats.find { it.id == chatId } ?: throw ChatNotFoundException())
    }

    fun readChatByMessageId(messageId: Long): Chat {
        return readChat(chats.find { it.messageIdsList.contains(messageId) } ?: throw ChatNotFoundException())
    }

    fun readChatByMessageCount(messageCount: Int): List<Chat> {
        val filteredChats = chats
            .filter { it.messageCount == messageCount }
        filteredChats.asSequence()
            .ifEmpty { throw ChatNotFoundException() }
            .forEach { readChat(it) }

        return filteredChats
    }

    fun deleteMessage(messageId: Long): Boolean {
        messages.find { it.id == messageId } ?: throw MessageNotFoundException()

        val chat = chats.find { it.messageIdsList.contains(messageId) } ?: throw ChatNotFoundException()

        chat.messageIdsList
            .filterNot { it == messageId }
            .ifEmpty {
                chats.remove(chat)
                return true
            }
            .let { chats.replaceItem(replacingItem = chat, newItem = chat.copy(messageIdsList = it)) }
            .let { return true }
    }

    fun deleteChat(chatId: Long): Boolean {
        val chat = chats.find { it.id == chatId } ?: throw ChatNotFoundException()

        messages.removeAll { chat.messageIdsList.contains(it.id) }
        return chats.remove(chat)
    }

    private fun updateChatMessageList(chat: Chat, message: Message): Boolean {

        chats
            .also { if (!it.contains(chat) || it.indexOf(chat) > it.lastIndex) it += chat }
            .let {
                it.set(
                    index = it.indexOf(chat),
                    element = chat.copy(messageIdsList = chat.messageIdsList + message.id, isRead = false)
                )
            }

        return true
    }

    private fun <E> MutableList<E>.replaceItem(replacingItem: E, newItem: E): E {
        this[this.indexOf(replacingItem)] = newItem
        return newItem
    }
}
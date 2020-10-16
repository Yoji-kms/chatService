package ru.netology

import ru.netology.exceptions.ChatNotFoundException
import ru.netology.exceptions.MessageNotFoundException
import java.lang.RuntimeException

object Service {
    private val messages = mutableListOf<Message>()
    private val chats = mutableListOf<Chat>()

    fun clear() {
        messages.clear()
        chats.clear()
    }

    fun getRandomChatId():Long{
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
        val chat: Chat = if (chats.isNotEmpty()) {
            chats.find { it.userIdsLst.containsAll(userIdsList) && it.userIdsLst.size == userIdsList.size }
                ?: createChat(userIdsList)
        } else createChat(userIdsList)

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
        val updatingMessage = messages.getItemByPredicate(
            predicate = { it.id == messageId },
            exception = MessageNotFoundException()
        )

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
        return readChat(
            chats.getItemByPredicate(
                predicate = { it.id == chatId },
                exception = ChatNotFoundException()
            )
        )
    }

    fun readChatByMessageId(messageId: Long): Chat {
        return readChat(
            chats.getItemByPredicate(
                predicate = { it.messageIdsList.contains(messageId) },
                exception = ChatNotFoundException()
            )
        )
    }

    fun readChatByMessageCount(messageCount: Int): List<Chat> {
        val filteredChats = chats.filter { it.messageCount == messageCount }
        if (filteredChats.isEmpty()) throw ChatNotFoundException()
        for (chat in filteredChats) {
            readChat(chat)
        }
        return filteredChats
    }

    fun deleteMessage(messageId: Long): Boolean {
        messages.getItemByPredicate(
            predicate = { it.id == messageId },
            exception = MessageNotFoundException()
        )

        val chat = chats.getItemByPredicate(
            predicate = { it.messageIdsList.contains(messageId) },
            exception = ChatNotFoundException()
        )
        val newMessageIdsList = chat.messageIdsList.filterNot { it == messageId }

        if (newMessageIdsList.isNotEmpty()) {
            chats.replaceItem(replacingItem = chat, newItem = chat.copy(messageIdsList = newMessageIdsList))
        } else chats.remove(chat)
        return true
    }

    fun deleteChat(chatId: Long): Boolean {
        val chat = chats.getItemByPredicate(
            predicate = { it.id == chatId },
            exception = ChatNotFoundException()
        )

        messages.removeAll { chat.messageIdsList.contains(it.id) }
        return chats.remove(chat)
    }

    private fun <E> MutableList<E>.getItemByPredicate(predicate: (E) -> Boolean, exception: RuntimeException): E {
        val result = find(predicate)
        if (result != null) return result
        throw exception
    }

    private fun updateChatMessageList(chat: Chat, message: Message): Boolean {
        if (chats.contains(chat)) if (chats.indexOf(chat) >= chats.lastIndex) {
            chats.replaceItem(
                replacingItem = chat,
                newItem = chat.copy(messageIdsList = chat.messageIdsList + message.id, isRead = false)
            )
            return true
        }
        chats += chat.copy(messageIdsList = chat.messageIdsList + message.id)
        return true
    }

    private fun <E> MutableList<E>.replaceItem(replacingItem: E, newItem: E): E {
        this[this.indexOf(replacingItem)] = newItem
        return newItem
    }
}
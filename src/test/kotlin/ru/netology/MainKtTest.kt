package ru.netology

import org.junit.Assert.*
import org.junit.Test
import ru.netology.exceptions.ChatNotFoundException
import ru.netology.exceptions.MessageNotFoundException

class MainKtTest{
    @Test
    fun main_createMessage(){
        val service = Service
        service.clear()

        assertNotNull(service.createMessage(fromId = 0, text = "some text", toIdsList = hashSetOf(1, 2)))
    }

    @Test
    fun main_editMessage(){
        val service = Service
        service.clear()

        val newText = "New text"
        val editingMessage = service.createMessage(fromId = 0, text = "some text", toIdsList = hashSetOf(1))
        service.createMessage(fromId = 1, text = "some text", toIdsList = hashSetOf(0, 2))

        assertEquals(service.editMessage(messageId = editingMessage.id, newText = newText).text, newText)
    }

    @Test(expected = MessageNotFoundException::class)
    fun main_editMessage_messageNotFound(){
        val service = Service
        service.clear()

        service.editMessage(messageId = Long.MAX_VALUE, newText = "New text")
    }

    @Test
    fun main_deleteMessage(){
        val service = Service
        service.clear()

        val deletingMessage = service.createMessage(fromId = 0, text = "some text", toIdsList = hashSetOf(1))
        service.createMessage(fromId = 1, text = "some text", toIdsList = hashSetOf(0))

        assertTrue(service.deleteMessage(deletingMessage.id))
    }

    @Test
    fun main_deleteLastMessageInChat(){
        val service = Service
        service.clear()

        val deletingMessage = service.createMessage(fromId = 0, text = "some text", toIdsList = hashSetOf(1))
        service.createMessage(fromId = 1, text = "some text", toIdsList = hashSetOf(0, 2))

        assertTrue(service.deleteMessage(deletingMessage.id))
    }

    @Test
    fun main_readChatById(){
        val service = Service
        service.clear()

        service.createMessage(fromId = 0, text = "some text", toIdsList = hashSetOf(1))
        service.createMessage(fromId = 1, text = "some text", toIdsList = hashSetOf(0, 2))

        val chatId = service.getRandomChatId()

        assertTrue(service.readChatById(chatId = chatId).isRead)
    }

    @Test
    fun main_readChatByMessageId(){
        val service = Service
        service.clear()

        val readingMessageId = service.createMessage(fromId = 0, text = "some text", toIdsList = hashSetOf(1)).id
        service.createMessage(fromId = 1, text = "some text", toIdsList = hashSetOf(0, 2))

        assertTrue(service.readChatByMessageId(readingMessageId).isRead)
    }

    @Test
    fun main_readChatByMessageCount(){
        val service = Service
        service.clear()

        //Всего 3 чата: 2 с одним сообщением и 1 - с двумя
        service.createMessage(fromId = 0, text = "some text", toIdsList = hashSetOf(1))
        service.createMessage(fromId = 1, text = "some text 2", toIdsList = hashSetOf(0))
        service.createMessage(fromId = 1, text = "some text", toIdsList = hashSetOf(0, 2))
        service.createMessage(fromId = 1, text = "some text 2", toIdsList = hashSetOf(2))

        assertEquals(2, service.readChatByMessageCount(1).size)
    }

    @Test(expected = ChatNotFoundException::class)
    fun main_readChatByMessageCount_chatNotFound(){
        val service = Service
        service.clear()

        //Всего 3 чата: 2 с одним сообщением и 1 - с двумя
        service.createMessage(fromId = 0, text = "some text", toIdsList = hashSetOf(1))
        service.createMessage(fromId = 1, text = "some text 2", toIdsList = hashSetOf(0))
        service.createMessage(fromId = 1, text = "some text", toIdsList = hashSetOf(0, 2))
        service.createMessage(fromId = 1, text = "some text 2", toIdsList = hashSetOf(2))

        service.readChatByMessageCount(3)
    }

    @Test
    fun main_deleteChat(){
        val service = Service
        service.clear()

        service.createMessage(fromId = 0, text = "some text", toIdsList = hashSetOf(1))
        service.createMessage(fromId = 1, text = "some text", toIdsList = hashSetOf(0, 2))

        val chatId = service.getRandomChatId()

        assertTrue(service.deleteChat(chatId))
    }
}
package ru.netology

data class Chat(
    val id: Long,
    val userIdsLst: HashSet<Long>,
    val messageIdsList: List<Long>,
    val unreadMessageIdsList: List<Long>,
    val isRead: Boolean = false
){
      val messageCount = messageIdsList.size
      val lastMessageId = messageIdsList.lastOrNull()
      val unreadMessageCount = unreadMessageIdsList.size
}
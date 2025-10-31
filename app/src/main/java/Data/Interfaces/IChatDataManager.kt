package Data.Interfaces

import Entity.Chat

/**
 * Contract that defines the data operations for the Chat entity.
 * This manager handles the metadata of a conversation, not the messages themselves.
 */
interface IChatDataManager {

  //CREATE
    fun addChat(chat: Chat)

  //READ
    fun getChatById(id: String): Chat?
    fun getChatByTicketId(ticketId: String): Chat?
    fun getChatsByUserId(userId: String): List<Chat>

   //UPDATE
    fun updateChat(chat: Chat)
}

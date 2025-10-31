package Data.Interfaces

import Entity.Message

/**
 * Contract that defines data operations for the Message entity.
 * This manager handles individual messages, which are typically part
 * of a subcollection within a Chat document.
 */
interface IMessageDataManager {

    //CREATE
    fun addMessage(chatId: String, message: Message)

 //READ
    fun getMessagesByChatId(chatId: String): List<Message>

    /**
     *  Listens for real-time updates to the messages in a chat.
     * function for building a live chat.
     * It would be implemented using Firestore's snapshot listeners if possible.
     */
    fun listenForMessages(chatId: String, onUpdate: (List<Message>) -> Unit)

}

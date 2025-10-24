package Entity

import Entity.enums.MessageType

/**
 * Represent unique message inside the chat
 */
data class Message(

    val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderRole: String = "", // ej: "client", "agent"

    /**
     * Message content
     */
    val content: String = "",

    val type: MessageType = MessageType.TEXT,
    val timestamp: Long = System.currentTimeMillis(),

    var isRead: Boolean = false
)

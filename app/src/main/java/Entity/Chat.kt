package Entity

/**
 * Represent the metadata inside the chat.
 * Messages are gonna be consulted through a subcollection in Firestore.
 */
data class Chat(
    val id: String = "",
    val ticketId: String = "",
    val userId: String = "",
    val agentId: String = "",
    val agentName: String = "",
    val createdAt: Long = System.currentTimeMillis(),


    var agentIsTyping: Boolean = false,
    var userIsTyping: Boolean = false,
    var updatedAt: Long = System.currentTimeMillis(),


    /**
     * The last text in the chat to show the chat list.
     * This field will be updated with a Cloud Function when sending the message
     */
    var lastMessageText: String = "",

    /**
     * The last message timestamp to tidy up the list in the chats
     */
    var lastMessageTimestamp: Long = 0,

    /**
     * Unread messages
     * This field will be also updated with a Cloud Function.
     */
    var unreadCountForUser: Int = 0
)

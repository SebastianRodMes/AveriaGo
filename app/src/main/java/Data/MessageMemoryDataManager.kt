package Data

import Data.Interfaces.IMessageDataManager
import Entity.Message

/**
 * In-memory implementation of the IMessageDataManager interface.
 * This manager uses a Map to store messages, where the key is the chat ID
 * and the value is the list of messages for that chat.
 */
object MessageMemoryDataManager : IMessageDataManager {

    // A Map where:
    // - Key (String) is the chatId.
    // - Value (MutableList<Message>) is the list of messages for that chat.
    private val messagesByChat = mutableMapOf<String, MutableList<Message>>()

    // init block to preload some sample messages for testing.
    init {
        loadSampleData()
    }

    /**
     *
     * If the chat doesn't have any messages yet, it creates a new list for it.
     */
    override fun addMessage(chatId: String, message: Message) {
        println("addMessage: Añadiendo mensaje al chat '$chatId'")
        // .getOrPut will get the list for the chatId, or create a new empty list if it doesn't exist.
        // Then we add the message to that list.
        messagesByChat.getOrPut(chatId) { mutableListOf() }.add(message)
    }


    override fun getMessagesByChatId(chatId: String): List<Message> {
        println("getMessagesByChatId: Buscando mensajes para el chat '$chatId'")
        // We find the list for the given chatId.
        // If the list is null (no messages for that chat), we return an empty list.
        // We also sort them by timestamp to ensure they appear in the correct order.
        return messagesByChat[chatId]?.sortedBy { it.timestamp } ?: emptyList()
    }

    /**
     * This is a simplified simulation for the in-memory manager.
     * In a real implementation (like with Firestore), this would set up a real-time listener.
     * Here, it just calls the callback once with the current data.
     */
    override fun listenForMessages(chatId: String, onUpdate: (List<Message>) -> Unit) {
        println("listenForMessages: Simulando escucha para el chat '$chatId'")
        // We immediately invoke the callback with the current list of messages.
        onUpdate(getMessagesByChatId(chatId))
    }

    /**
     * Private function to populate the map with sample data.
     */
    private fun loadSampleData() {
        val chatId1 = "CHAT-001" // Linked to TCK-001 from TicketMemoryDataManager
        val chatId2 = "CHAT-002" // Linked to TCK-002

        messagesByChat[chatId1] = mutableListOf(
            Message(
                messageId = "MSG-001",
                senderId = "USR-001", // The user
                content = "Hola, mi internet está muy lento.",
                timestamp = System.currentTimeMillis() - 600000L // 10 minutes ago
            ),
            Message(
                messageId = "MSG-002",
                senderId = "AGENT-01", // The agent
                content = "Hola Juan, gracias por contactarnos. ¿Podrías reiniciar tu router, por favor?",
                timestamp = System.currentTimeMillis() - 300000L // 5 minutes ago
            )
        )

        messagesByChat[chatId2] = mutableListOf(
            Message(
                messageId = "MSG-003",
                senderId = "USR-001", // The user
                content = "Mi tele no tiene señal.",

            )
        )
        println("MessageMemoryDataManager: Datos de ejemplo cargados para ${messagesByChat.size} chats.")
    }
}

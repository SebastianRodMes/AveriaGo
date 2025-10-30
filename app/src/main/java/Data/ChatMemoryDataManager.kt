package Data

import Data.Interfaces.IChatDataManager
import Entity.Chat

/**
 * In-memory implementation of the IChatDataManager interface.
 * This manager handles a list of chat metadata objects, matching the Chat entity.
 */
object ChatMemoryDataManager : IChatDataManager {

    private val chatList = mutableListOf<Chat>()

    // init block to preload some sample chats for testing.

    init {
        loadSampleData()
    }


    override fun addChat(chat: Chat) {
        println("addChat: Añadiendo chat con ID ${chat.id}")
        chatList.add(chat)
    }


    override fun getChatById(id: String): Chat? {
        println("getChatById: Buscando chat con ID '$id'")
        return chatList.find { it.id == id }
    }


    override fun getChatByTicketId(ticketId: String): Chat? {
        println("getChatByTicketId: Buscando chat para el ticket '$ticketId'")
        return chatList.find { it.ticketId == ticketId }
    }


    override fun getChatsByUserId(userId: String): List<Chat> {
        println("getChatsByUserId: Buscando chats para el usuario '$userId'")
        // Now we filter directly by the 'userId' field.
        return chatList.filter { it.userId == userId }
    }


    override fun updateChat(chat: Chat) {
        println("updateChat: Actualizando chat con ID ${chat.id}")
        val index = chatList.indexOfFirst { it.id == chat.id }
        if (index != -1) {
            chatList[index] = chat
            println("updateChat: ¡Chat actualizado con éxito!")
        } else {
            println("updateChat: Error - No se encontró el chat para actualizar.")
        }
    }


    private fun loadSampleData() {
        chatList.add(
            Chat(
                id = "CHAT-001",
                ticketId = "TCK-001",
                userId = "USR-001",
                agentId = "AGENT-01",
                agentName = "Agente Smith",
                lastMessageText = "Hola Juan, gracias por contactarnos. ¿Podrías reiniciar tu router, por favor?",
                lastMessageTimestamp = System.currentTimeMillis() - 300000L, // 5 minutes ago
                unreadCountForUser = 1
            )
        )
        chatList.add(
            Chat(
                id = "CHAT-002",
                ticketId = "TCK-002",
                userId = "USR-001",
                agentId = "AGENT-02",
                agentName = "Agente Jones",
                lastMessageText = "Mi tele no tiene señal.",
                lastMessageTimestamp = System.currentTimeMillis() - 900000L // 15 minutes ago
            )
        )
        println("ChatMemoryDataManager: Datos de ejemplo cargados para ${chatList.size} chats.")
    }

    // Helper function for testing purposes
    fun getAllChatsForTest(): List<Chat> {
        return chatList.toList()
    }
}

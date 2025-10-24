package testing

import Data.ChatMemoryDataManager
import Entity.Chat

/**
 * This main function allows us to execute and test the logic of the
 * ChatMemoryDataManager directly in the console.
 */
fun main() {
    println("--- INICIANDO PRUEBAS DEL ChatMemoryDataManager ---")

    val manager = ChatMemoryDataManager

    // 1. Test fetching all initial chats.
    // This shows the sample data loaded in the manager's init block.
    println("\n[PASO 1: OBTENER TODOS LOS CHATS INICIALES]")
    val initialChats = manager.getAllChatsForTest() // Using our test helper function
    println("Total de chats iniciales: ${initialChats.size}")
    initialChats.forEach { chat ->
        println("  - Chat ID: ${chat.id}, Ticket ID: ${chat.ticketId}")
    }

    // 2. Test getting a chat by its ID.
    println("\n[PASO 2: BUSCAR CHAT POR SU ID]")
    val foundById = manager.getChatById("CHAT-001")
    println("Chat encontrado por ID 'CHAT-001': ${foundById?.lastMessageText ?: "Ninguno"}")

    // 3. Test getting a chat by its associated Ticket ID.
    println("\n[PASO 3: BUSCAR CHAT POR TICKET ID]")
    val foundByTicketId = manager.getChatByTicketId("TCK-002")
    println("Chat encontrado para el ticket 'TCK-002': ${foundByTicketId?.lastMessageText ?: "Ninguno"}")

    // 4. Test getting all chats for a specific user.
    println("\n[PASO 4: BUSCAR CHATS POR USER ID]")
    val userChats = manager.getChatsByUserId("USR-001")
    println("Chats encontrados para el usuario 'USR-001': ${userChats.size}")
    userChats.forEach { chat ->
        println("  - Chat ID: ${chat.id}, Último mensaje: \"${chat.lastMessageText}\"")
    }

    // 5. Test updating a chat.
    // This simulates what happens when a new message is sent.
    println("\n[PASO 5: ACTUALIZAR UN CHAT]")
    val chatToUpdate = foundById!!.copy(
        lastMessageText = "Ya lo reinicié, pero sigo sin conexión.",
        lastMessageTimestamp = System.currentTimeMillis(),
        unreadCountForUser = 0, // Agent saw the message
        unreadCountForAgent = 1 // New message for the agent
    )
    manager.updateChat(chatToUpdate)
    val updatedChat = manager.getChatById("CHAT-001")
    println("Último mensaje del chat 'CHAT-001' después de actualizar: \"${updatedChat?.lastMessageText}\"")

    // 6. Test searching for a non-existent chat.
    println("\n[PASO 6: BUSCAR CHAT INEXISTENTE]")
    val nonExistentChat = manager.getChatById("FAKE-CHAT-999")
    if (nonExistentChat == null) {
        println("Resultado esperado: No se encontró el chat 'FAKE-CHAT-999'. ¡Prueba exitosa!")
    } else {
        println("Error en la prueba: Se encontró un chat que no debería existir.")
    }

    println("\n--- PRUEBAS FINALIZADAS ---")
}

package testing

import Data.MessageMemoryDataManager
import Entity.Message


/**
 * This main function allows us to execute and test the logic of our
 * MessageMemoryDataManager directly in the console.
 */
fun main() {
    println("--- INICIANDO PRUEBAS DEL MessageMemoryDataManager ---")

    val manager = MessageMemoryDataManager

    // We will use this chat ID for most of our tests.
    // It corresponds to the sample data for "TCK-001".
    val testChatId = "CHAT-001"

    // 1. Test fetching messages for a specific chat.
    // This should return the two sample messages for CHAT-001, sorted by time.
    println("\n[PASO 1: OBTENER MENSAJES POR CHAT ID]")
    val initialMessages = manager.getMessagesByChatId(testChatId)

    println("Mensajes encontrados para el chat '$testChatId': ${initialMessages.size}")
    initialMessages.forEach { message ->
        println("  -> [${message.senderId}]: ${message.content}")
    }

    // 2. Test adding a new message to the chat.
    println("\n[PASO 2: AÑADIR NUEVO MENSAJE]")
    val newMessage = Message(
        messageId = "MSG-004",
        senderId = "USR-001", // The user responds
        content = "Ok, ya reinicié el router. Parece que sigue igual.",
        timestamp = System.currentTimeMillis()
    )
    manager.addMessage(testChatId, newMessage)

    val messagesAfterAdd = manager.getMessagesByChatId(testChatId)
    println("Total de mensajes en '$testChatId' después de añadir: ${messagesAfterAdd.size}")
    val lastMessage = messagesAfterAdd.last()
    println("Último mensaje añadido: \"${lastMessage.content}\"")

    // 3. Test the simulated listener.
    // In our in-memory version, this will just fetch the current data once
    // and execute the code inside the lambda.
    println("\n[PASO 3: PROBAR EL LISTENER SIMULADO]")
    manager.listenForMessages(testChatId) { updatedMessages ->
        println("  -> El listener se activó para el chat '$testChatId'.")
        println("  -> Se recibieron ${updatedMessages.size} mensajes.")
        println("  -> Mensaje más reciente: [${updatedMessages.last().senderId}] - \"${updatedMessages.last().content}\"")
    }

    // 4. Test fetching messages for a chat with no messages (or a non-existent chat).
    // The manager should return an empty list gracefully.
    println("\n[PASO 4: OBTENER MENSAJES DE UN CHAT VACÍO]")
    val emptyChatId = "CHAT-INEXISTENTE"
    val emptyMessages = manager.getMessagesByChatId(emptyChatId)
    if (emptyMessages.isEmpty()) {
        println("Resultado esperado: La lista de mensajes para '$emptyChatId' está vacía. ¡Prueba exitosa!")
    } else {
        println("Error en la prueba: Se encontraron mensajes para un chat que no debería tenerlos.")
    }


    println("\n--- PRUEBAS FINALIZADAS ---")
}

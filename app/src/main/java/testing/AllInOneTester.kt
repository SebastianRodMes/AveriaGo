package testing

import Data.*
import Data.Interfaces.ICategoryDataManager
import Data.Interfaces.IChatDataManager
import Data.Interfaces.IMessageDataManager
import Data.Interfaces.ITicketDataManager
import Entity.Ticket
import Entity.Message

import java.util.UUID

/**
 *  all-in-one console tester that simulates
 * the complete application flow, allowing a user to perform actions
 * through a menu, this is pretty much to see and test that everything is working as it is supposed to.
 */
fun main() {
    println("--- BIENVENIDO AL SIMULADOR DE FLUJO DE AVERIAGO ---")

    // Initialize all our data managers
    val userManager = UserMemoryDataManager
    val newsManager = NewsMemoryDataManager
    val categoryManager = CategoryMemoryDataManager
    val ticketManager = TicketMemoryDataManager
    val chatManager = ChatMemoryDataManager
    val messageManager = MessageMemoryDataManager

    //  simulate we are logged in as this user
    val currentUser = userManager.getUserById("USR-001")
    if (currentUser == null) {
        println("Error fatal: No se pudo encontrar el usuario de prueba 'USR-001'.")
        return
    }
    println("Simulando sesión iniciada como: ${currentUser.fullName} (${currentUser.email})")

    // --- Main application loop ---
    while (true) {
        printMenu()
        print("\nElige una opción: ")
        val choice = readlnOrNull()?.toIntOrNull()

        when (choice) {
            1 -> viewNews(newsManager)
            2 -> createNewTicket(currentUser.userId, categoryManager, ticketManager, chatManager)
            3 -> viewMyTickets(currentUser.userId, ticketManager)
            4 -> viewAndManageChat(currentUser.userId, ticketManager, chatManager, messageManager)
            0 -> {
                println("Saliendo del simulador. ¡Hasta luego!")
                return
            }
            else -> println("Opción no válida, por favor intenta de nuevo.")
        }
        println("\n--- Presiona Enter para continuar ---")
        readln()
    }
}

/** Prints the main menu options. */
fun printMenu() {
    println("\n====================== MENÚ PRINCIPAL ======================")
    println("1. Ver noticias y averías generales")
    println("2. Crear un nuevo ticket de avería")
    println("3. Ver mis tickets abiertos")
    println("4. Entrar al chat de un ticket")
    println("0. Salir")
    println("==========================================================")
}

/** Simulates viewing the news feed. */
fun viewNews(newsManager: NewsMemoryDataManager) {
    println("\n--- NOTICIAS Y AVERÍAS GENERALES ---")
    val allNews = newsManager.getAllNews()
    if (allNews.isEmpty()) {
        println("No hay noticias en este momento.")
        return
    }
    allNews.forEach { news ->
        println("[${news.type}] - ${news.title}: ${news.description}")
    }
}

/** Simulates the flow of creating a new ticket. */
fun createNewTicket(userId: String, categoryManager: ICategoryDataManager, ticketManager: ITicketDataManager, chatManager: IChatDataManager) {
    println("\n--- CREAR NUEVO TICKET ---")
    // 1. Show categories
    val categories = categoryManager.getAllCategories()
    categories.forEachIndexed { index, category ->
        println("${index + 1}. ${category.name}")
    }
    print("Elige una categoría: ")
    val catChoice = readlnOrNull()?.toIntOrNull()
    if (catChoice == null || catChoice !in 1..categories.size) {
        println("Selección inválida.")
        return
    }
    val selectedCategory = categories[catChoice - 1]

    // 2. Show subcategories
    println("\n--- Subcategorías de ${selectedCategory.name} ---")
    val subcategories = selectedCategory.subcategories
    subcategories.forEachIndexed { index, subcategory ->
        println("${index + 1}. ${subcategory.name}")
    }
    print("Elige una subcategoría: ")
    val subCatChoice = readlnOrNull()?.toIntOrNull()
    if (subCatChoice == null || subCatChoice !in 1..subcategories.size) {
        println("Selección inválida.")
        return
    }
    val selectedSubcategory = subcategories[subCatChoice - 1]

    // 3. Get description
    print("Describe tu problema: ")
    val description = readlnOrNull() ?: "Sin descripción"
    //  Get location
    print("Ubicacion: ")
    val location = readlnOrNull() ?: "Sin direccion"

    // 4. Create the Ticket and the Chat
    val newTicketId = "TCK-${UUID.randomUUID().toString().take(4).uppercase()}"
    val newTicket = Ticket(
        ticketId = newTicketId,
        userId = userId,
        category = selectedCategory.name,
        subcategory = selectedSubcategory.name,
        description = description,
        location = location
    )
    ticketManager.addTicket(newTicket)
    println(">> ¡Ticket $newTicketId creado con éxito!")

    // Automatically create a corresponding chat
    val newChat = Entity.Chat(
        id = "CHAT-${newTicketId.split('-')[1]}",
        ticketId = newTicketId,
        userId = userId,
        agentName = "Esperando agente...",
        lastMessageText = "Ticket creado. Describe tu problema aquí."
    )
    chatManager.addChat(newChat)
    println(">> ¡Chat para el ticket $newTicketId creado!")
}

/** Simulates viewing the list of user's own tickets. */
fun viewMyTickets(userId: String, ticketManager: ITicketDataManager) {
    println("\n--- MIS TICKETS ---")
    val myTickets = ticketManager.getTicketsByUserId(userId)
    if (myTickets.isEmpty()) {
        println("No tienes ningún ticket registrado.")
        return
    }
    myTickets.forEach { ticket ->
        println(" - ID: ${ticket.ticketId} | Estado: ${ticket.status} | Problema: ${ticket.description}")
    }
}

/** Simulates entering a chat, viewing messages and sending one. */
fun viewAndManageChat(userId: String, ticketManager: ITicketDataManager, chatManager: IChatDataManager, messageManager: IMessageDataManager) {
    println("\n--- SELECCIONAR CHAT DE TICKET ---")
    val myTickets = ticketManager.getTicketsByUserId(userId)
    if (myTickets.isEmpty()) {
        println("No tienes tickets para ver el chat.")
        return
    }
    myTickets.forEachIndexed { index, ticket ->
        println("${index + 1}. Ticket ${ticket.ticketId} (${ticket.description})")
    }
    print("Elige un ticket para ver su chat: ")
    val ticketChoice = readlnOrNull()?.toIntOrNull()
    if (ticketChoice == null || ticketChoice !in 1..myTickets.size) {
        println("Selección inválida.")
        return
    }
    val selectedTicket = myTickets[ticketChoice - 1]

    // Find the corresponding chat
    val chat = chatManager.getChatByTicketId(selectedTicket.ticketId)
    if (chat == null) {
        println("Error: No se encontró un chat para este ticket.")
        return
    }

    // Show messages
    println("\n--- CHAT DEL TICKET ${chat.ticketId} (Agente: ${chat.agentName}) ---")
    val messages = messageManager.getMessagesByChatId(chat.id)
    if (messages.isEmpty()) {
        println("(Aún no hay mensajes en este chat)")
    } else {
        messages.forEach { msg ->
            println("[${msg.senderId}]: ${msg.content}")
        }
    }
    println("---------------------------------------------------------")

    // Send a message
    print("Escribe tu mensaje (o presiona Enter para volver): ")
    val textToSend = readlnOrNull()
    if (!textToSend.isNullOrBlank()) {
        val newMessage = Message(
            messageId = "MSG-${UUID.randomUUID().toString().take(4).uppercase()}",
            senderId = userId,
            content = textToSend,
            timestamp = System.currentTimeMillis()
        )
        messageManager.addMessage(chat.id, newMessage)
        // IMPORTANT: Update the chat's last message
        val updatedChat = chat.copy(
            lastMessageText = textToSend,
            lastMessageTimestamp = newMessage.timestamp
        )
        chatManager.updateChat(updatedChat)
        println(">> Mensaje enviado.")
    }
}

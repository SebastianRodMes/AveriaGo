package testing

import Data.TicketMemoryDataManager
import Entity.Ticket
import Entity.enums.TicketStatus

/**
 * This main function allows us to execute and test the logic of our
 * TicketMemoryDataManager directly in the console.
 */
fun main() {
    println("--- INICIANDO PRUEBAS DEL TicketMemoryDataManager ---")

    val manager = TicketMemoryDataManager

    // 1. Test getting all initial tickets.
    // This will show the sample data loaded in the manager's init block.
    println("\n[PASO 1: OBTENER TODOS LOS TICKETS INICIALES]")
    val initialTickets = manager.getAllTickets()
    println("Total de tickets iniciales: ${initialTickets.size}")
    initialTickets.forEach { println("  - ID: ${it.ticketId}, Status: ${it.status}, Usuario: ${it.userId}") }

    // 2. Test adding a new ticket.
    // As you correctly pointed out, we don't need to specify the status.
    // It will automatically use the default value from the data class.
    println("\n[PASO 2: AÑADIR NUEVO TICKET]")
    val newTicket = Ticket(
        ticketId = "TCK-004",
        userId = "USR-002",
        category = "Soporte General",
        subcategory = "Consulta de facturación",
        description = "Tengo una duda sobre mi última factura.",
        location = "Heredia, Santo Domingo"
    )
    manager.addTicket(newTicket)
    println("Ticket añadido. Verificando su estado por defecto...")
    val addedTicket = manager.getTicketById("TCK-004")
    println("  -> El estado del nuevo ticket es: ${addedTicket?.status}. ¡Prueba exitosa!")
    println("Total de tickets después de añadir: ${manager.getAllTickets().size}")

    // 3. Test getting a ticket by its ID.
    println("\n[PASO 3: BUSCAR TICKET POR ID]")
    val foundTicket = manager.getTicketById("TCK-002")
    println("Ticket encontrado 'TCK-002': ${foundTicket?.description ?: "Ninguno"}")

    // 4. Test getting all tickets for a specific user.
    println("\n[PASO 4: BUSCAR TICKETS POR USER ID]")
    val userTickets = manager.getTicketsByUserId("USR-001")
    println("Tickets encontrados para el usuario 'USR-001': ${userTickets.size}")
    userTickets.forEach { println("  - ID: ${it.ticketId}, Descripción: ${it.description}") }

    // 5. Test updating a ticket's status.
    println("\n[PASO 5: ACTUALIZAR UN TICKET]")
    // create a modified version of the object
    val ticketToUpdate = foundTicket!!.copy(status = TicketStatus.SOLVED)
    manager.updateTicket(ticketToUpdate)
    val updatedTicket = manager.getTicketById("TCK-002")
    println("Estado del ticket 'TCK-002' después de actualizar: ${updatedTicket?.status}")

    // 6. Test removing a ticket.
    println("\n[PASO 6: ELIMINAR UN TICKET]")
    manager.deleteTicket("TCK-004")
    println("Total de tickets al final: ${manager.getAllTickets().size}")

    println("\n--- PRUEBAS FINALIZADAS ---")
}

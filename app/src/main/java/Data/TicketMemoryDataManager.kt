package Data

import Data.Interfaces.ITicketDataManager
import Entity.Ticket
import Entity.enums.TicketStatus

object TicketMemoryDataManager : ITicketDataManager {
    private val ticketList = mutableListOf<Ticket>()

    // Block to initialize with sample data
    init {

        ticketList.add(
            Ticket(
                ticketId = "TCK-001",
                userId = "USR-001", // User Juan Pérez from the other example
                category = "Internet",
                subcategory = "Baja velocidad",
                description = "El internet está muy lento desde ayer, no carga ni los videos.",
                assignedAgentId = "AGENT-01",
                location = "Alajuela, Orotina"
            )
        )
        ticketList.add(
            Ticket(
                ticketId = "TCK-002",
                userId = "USR-001", // Another ticket for the same user
                category = "Televisión",
                subcategory = "Sin señal",
                description = "La pantalla se queda en negro, dice 'Sin Señal'.",
                assignedAgentId = "AGENT-02",
                assignedTechnicianId = "TECH-A",
                location = "Puntarenas, El Roble"
            )
        )
        ticketList.add(
            Ticket(
                ticketId = "TCK-003",
                userId = "USR-002", // Ticket for user Ana López
                category = "Internet",
                subcategory = "No hay conexión",
                description = "La luz del router está en rojo y no tengo acceso a internet.",
              location = "Cartago, Oreamuno"
            )
        )
    }

    override fun addTicket(ticket: Ticket) {
        println("addTicket: Añadiendo ticket con ID ${ticket.ticketId}")
        ticketList.add(ticket)
    }

    override fun getTicketById(id: String): Ticket? {
        println("getTicketById: Buscando ticket con ID '$id'")
        // It returns the first element that matches the condition, or null if not found.
        return ticketList.find { it.ticketId == id }
    }

    override fun getAllTickets(): List<Ticket> {
        println("getAllTickets: Devolviendo todos los tickets (${ticketList.size} en total)")
        return ticketList.toList() // Return a copy of the list
    }

    override fun getTicketsByUserId(userId: String): List<Ticket> {
        println("getTicketsByUserId: Buscando tickets para el usuario '$userId'")
        // .filter returns a new list containing all elements that match the condition.
        return ticketList.filter { it.userId == userId }
    }

    override fun updateTicket(ticket: Ticket) {
        println("updateTicket: Actualizando ticket con ID ${ticket.ticketId}")
        val index = ticketList.indexOfFirst { it.ticketId == ticket.ticketId }
        if (index != -1) {
            ticketList[index] = ticket // Replace the old object with the new one
            println("updateTicket: ¡Ticket actualizado con éxito!")
        } else {
            println("updateTicket: Error - No se encontró el ticket para actualizar.")
        }
    }

    override fun deleteTicket(id: String) {
        println("removeTicket: Intentando eliminar ticket con ID '$id'")
        val removed = ticketList.removeIf { it.ticketId == id }
        if (removed) {
            println("removeTicket: ¡Ticket eliminado con éxito!")
        } else {
            println("removeTicket: Error - No se encontró el ticket para eliminar.")
        }
    }
}

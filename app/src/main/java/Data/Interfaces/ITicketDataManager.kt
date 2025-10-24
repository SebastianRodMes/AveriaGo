package Data.Interfaces

import Entity.Ticket

/**
 * Contract that defines the data operations for the Ticket entity.
 * Any class that manages Tickets (in memory, on Firestore, etc.)
 * must implement this interface.
 */
interface ITicketDataManager {
    // CREATE
    fun addTicket(ticket: Ticket)

    // READ
    fun getTicketById(id: String): Ticket?
    fun getAllTickets(): List<Ticket>
    fun getTicketsByUserId(userId: String): List<Ticket>

    // UPDATE
    fun updateTicket(ticket: Ticket)

    // DELETE (This is optional, tickets are usually not deleted if not they're canceled)
    fun removeTicket(id: String)
}
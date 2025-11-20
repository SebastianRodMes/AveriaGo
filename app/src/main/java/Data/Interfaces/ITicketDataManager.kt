package Data.Interfaces

import Entity.Ticket

/**
 * Contract that defines the data operations for the Ticket entity.
 */
interface ITicketDataManager {
    // CREATE
    fun addTicket(ticket: Ticket)

    // READ
    fun getTicketById(id: String): Ticket?
    fun getTicketsByUserId(userId: String): List<Ticket>?
    fun getAllTickets(): List<Ticket>?

    // UPDATE
    fun updateTicket(ticket: Ticket)

    // DELETE
    fun deleteTicket(id: String)
}
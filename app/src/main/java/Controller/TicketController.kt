package Controller

import Data.Interfaces.ITicketDataManager
import Data.SharedPrefsTicketDataManager
import Entity.Ticket
import android.content.Context

class TicketController(
    private val context: Context,
    private val ticketManager: ITicketDataManager
) {
    // Constructor secundario que inyecta la implementación por defecto (SharedPrefs)
    constructor(context: Context) : this(context, SharedPrefsTicketDataManager.getInstance(context))

    fun addTicket(ticket: Ticket) {
        try {
            ticketManager.addTicket(ticket)
        } catch (e: Exception) {
            throw Exception("Error al agregar el ticket: ${e.message}")
        }
    }

    fun getTicketById(id: String): Ticket? {
        try {
            return ticketManager.getTicketById(id)
        } catch (e: Exception) {
            throw Exception("Error al obtener el ticket")
        }
    }

    fun getTicketsByUserId(userId: String): List<Ticket>? {
        try {
            return ticketManager.getTicketsByUserId(userId)
        } catch (e: Exception) {
            throw Exception("Error al obtener los tickets del usuario")
        }
    }

    fun getAllTickets(): List<Ticket>? {
        try {
            return ticketManager.getAllTickets()
        } catch (e: Exception) {
            throw Exception("Error al obtener los tickets")
        }
    }

    fun updateTicket(ticket: Ticket) {
        try {
            ticketManager.updateTicket(ticket)
        } catch (e: Exception) {
            throw Exception("Error al actualizar el ticket")
        }
    }

    fun deleteTicket(id: String) {
        try {
            ticketManager.deleteTicket(id)
        } catch (e: Exception) {
            throw Exception("Error al eliminar el ticket")
        }
    }
}
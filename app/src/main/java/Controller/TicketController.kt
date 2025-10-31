package Controller

import Data.Interfaces.ITicketDataManager
import Data.TicketMemoryDataManager
import Entity.Ticket
import android.content.Context

class TicketController (private val context: Context){
    private var ticketManager: ITicketDataManager = TicketMemoryDataManager
    fun addTicket(ticket: Ticket){
        try {
            ticketManager.addTicket(ticket)
        }catch (e: Exception){
            throw Exception("Error al agregar el ticket")

        }
    }

    // READ
    fun getTicketById(id: String): Ticket{
        try {
            var result = ticketManager.getTicketById(id)
            if (result == null) {
                throw Exception("No se encontro el ticket")
            }
            return result
        }catch (e: Exception){
            throw Exception("Error al obtener el ticket")
        }
    }
    fun getAllTickets(): List<Ticket>{
        try {
            var result = ticketManager.getAllTickets()
            if (result == null) {
                throw Exception("No se encontraron tickets")
            }
            return result
            }catch (e: Exception){
            throw Exception("Error al obtener los tickets")
        }

    }
    fun getTicketsByUserId(userId: String): List<Ticket>{
        try {
            var result = ticketManager.getTicketsByUserId(userId)
            if (result == null) {
                throw Exception("No se encontraron tickets")
            }
            return result
            }catch (e: Exception){
            throw Exception("Error al obtener los tickets")
        }
    }

    // UPDATE
    fun updateTicket(ticket: Ticket){
        try {
            var ticketExists = ticketManager.getTicketById(ticket.ticketId)
            if (ticketExists == null) {
                throw Exception("No se encontro el ticket")
            }
            ticketManager.updateTicket(ticket)
            }catch (e: Exception){
            throw Exception("Error al actualizar el ticket")
            }
        }






}
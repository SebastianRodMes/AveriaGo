package Data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken as GsonTypeToken
import Data.Interfaces.ITicketDataManager
import Entity.Ticket

class SharedPrefsTicketDataManager private constructor(private val context: Context) : ITicketDataManager {

    companion object {
        private const val PREF_NAME = "AveriaguApp_tickets"
        private const val KEY_TICKETS = "tickets_json"
        @Volatile
        private var INSTANCE: SharedPrefsTicketDataManager? = null

        fun getInstance(context: Context): SharedPrefsTicketDataManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPrefsTicketDataManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val prefs: SharedPreferences
        get() = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()
    private val listType = object : GsonTypeToken<MutableList<Ticket>>() {}.type

    private fun loadTickets(): MutableList<Ticket> {
        val json = prefs.getString(KEY_TICKETS, null) ?: return mutableListOf()
        return try {
            gson.fromJson(json, listType) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    private fun saveTickets(list: List<Ticket>) {
        prefs.edit().putString(KEY_TICKETS, gson.toJson(list)).apply()
    }

    override fun addTicket(ticket: Ticket) {
        val list = loadTickets()
        list.add(ticket)
        saveTickets(list)
    }

    override fun getTicketById(id: String): Ticket? {
        val list = loadTickets()
        return list.firstOrNull { it.ticketId == id }
    }

    override fun getTicketsByUserId(userId: String): List<Ticket>? {
        val list = loadTickets()
        return list.filter { it.userId == userId }
    }

    override fun getAllTickets(): List<Ticket>? {
        return loadTickets()
    }

    override fun updateTicket(ticket: Ticket) {
        val list = loadTickets()
        val idx = list.indexOfFirst { it.ticketId == ticket.ticketId }
        if (idx >= 0) {
            list[idx] = ticket
            saveTickets(list)
        } else {
            throw Exception("Ticket no encontrado")
        }
    }

    override fun deleteTicket(id: String) {
        val list = loadTickets()
        val removed = list.removeAll { it.ticketId == id }
        if (removed) {
            saveTickets(list)
        } else {
            throw Exception("Ticket no encontrado")
        }
    }
}
package Entity

import Entity.enums.Rating
import Entity.enums.TicketStatus

data class Ticket (
    val ticketId: String, //  We assume that it is given when creating the object
    val userId: String,
    val category: String,
    val subcategory: String,
    val description: String,
    val location: String,
    val createdAt: Long = System.currentTimeMillis(),


    var status: TicketStatus = TicketStatus.PENDING,
    var priority: String = "medium",
    var updatedAt: Long = System.currentTimeMillis(),

    var assignedAgentId: String = "",
    var assignedTechnicianId: String = "",

    var photos: MutableList<String> = mutableListOf(),
    var timeline: MutableList<TimelineEvent> = mutableListOf(),

    var closedAt: Long? = null,
    var rating: Calification? = null
) {
    fun isActive(): Boolean = status != TicketStatus.CLOSED && status != TicketStatus.CANCELLED

    fun getFormattedId(): String = "#$ticketId"

    fun addTimelineEvent(event: TimelineEvent) {
        timeline.add(event)
        updatedAt = System.currentTimeMillis()
    }
}

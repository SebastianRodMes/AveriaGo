package Entity

import Entity.enums.TicketStatus

data class TimelineEvent(
    // Si el evento implica un cambio de estado, aquí va el nuevo estado.
    // Puede ser null para eventos informativos (comentarios, fotos, etc.).
    val status: TicketStatus? = null,

    // Id del actor (userId, agentId, "system", etc.)
    val actorId: String = "",

    // Nombre legible del actor (opcional)
    val actorName: String? = null,

    // Rol del actor ("cliente", "agente", "tecnico", "sistema", ...)
    val actorRole: String? = null,

    // Mensaje libre describiendo el evento (opcional)
    val message: String? = null,

    // URLs / paths de fotos o archivos relacionados con el evento
    val attachments: MutableList<String> = mutableListOf(),

    // Momento del evento
    val timestamp: Long = System.currentTimeMillis()
)
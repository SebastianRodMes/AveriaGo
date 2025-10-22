package Entity

import Entity.enums.TicketStatus

data class TimelineEvent(
    /**
     * El nuevo estado que se estableció en este evento.
     */
    val status: TicketStatus,

    /**
     * El nombre de la persona o entidad que generó el evento.
     * Ejemplo: "Juan Pérez" (cliente), "María López" (agente), "Sistema".
     */
    val actorName: String,

    /**
     * El rol de la persona que generó el evento.
     * Esto permite mostrar iconos o textos diferentes en la UI.
     * Ejemplo: "cliente", "agente", "tecnico", "sistema".
     */
    val actorRole: String,


    /**
     * El momento exacto en que ocurrió el evento. Por defecto, es el momento de la creación.
     */
    val timestamp: Long = System.currentTimeMillis()
)
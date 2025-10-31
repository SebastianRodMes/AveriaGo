package Entity.enums

enum class TicketStatus(val displayName: String, val color: String) {
    PENDING("Pendiente", "#F59E0B"),
    IN_ATTENTION("En Atención", "#3B82F6"),
    RESOLVED_REMOTELY("Resuelto Remotamente", "#10B981"),
    TECHNICIAN_ASSIGNED("Técnico Asignado", "#F97316"),
    IN_PROGRESS("En Progreso", "#EF4444"),
    CLOSED("Cerrado", "#059669"),
    CANCELLED("Cancelado", "#6B7280"),
    SOLVED("Resuelto", "")
}

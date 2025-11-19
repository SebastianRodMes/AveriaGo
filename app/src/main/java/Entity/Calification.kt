package Entity

import Entity.enums.Rating

data class Calification(
    var rating: Rating? = null,           // null = aún no calificado
    var comment: String = "",             // comentario opcional
    val raterId: String? = null,          // userId o agente que hizo la calificación (opcional)
    val createdAt: Long = System.currentTimeMillis() // timestamp de creación
)
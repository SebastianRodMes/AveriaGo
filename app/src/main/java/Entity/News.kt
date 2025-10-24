package Entity

/**
 * Represent the news either maintenance warnings or massive outage
 */
data class News(

    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "", // "outage", "news", "maintenance"
    val affectedServices: List<String> = emptyList(),
    val affectedZones: List<AffectedZone> = emptyList(),
    val startDate: Long = 0,
    val endDate: Long = 0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if the news is currently within its time window
     *  and marked as active. Ideal for filtering what news to show the user.
     */
    fun isHappeningNow(): Boolean {
        val now = System.currentTimeMillis()
        // Una noticia sin fecha de fin (endDate = 0) se considera activa indefinidamente
        val isWithinTime = now >= startDate && (endDate == 0L || now <= endDate)
        return isActive && isWithinTime
    }
}

/**
 * Represents a specific geographic area affected by a news.
 */
data class AffectedZone(
    val name: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val radius: Int = 0 // Radio en metros
)

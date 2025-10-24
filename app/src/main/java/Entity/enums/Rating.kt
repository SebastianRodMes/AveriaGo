package Entity.enums

enum class Rating(val score: Int, val description: String) {
    EXCELLENT(5, "Excelente"),
    GOOD(4, "Buena"),
    AVERAGE(3, "Normal"),
    LOW(2, "Baja"),
    POOR(1, "Pésima");
}

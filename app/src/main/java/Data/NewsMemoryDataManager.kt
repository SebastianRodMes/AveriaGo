package Data

import Data.Interfaces.INewsDataManager
import Entity.News

/**
 * Implementación del contrato INewsDataManager que guarda los datos
 * en una simple lista en memoria. Los datos se pierden si la app se cierra.
 *  desarrollo y pruebas iniciales.
 */
object NewsMemoryDataManager : INewsDataManager {

    // Lista en memoria que contendrá todas las noticias.
    private val newsList = mutableListOf<News>()

    // Bloque inicializador para cargar datos de ejemplo al arrancar la app.
    // Así la lista no estará vacía cuando la consultes por primera vez.
    init {
        loadSampleData()
    }

    /**
     * Devuelve una copia de la lista de noticias.
     */
    override fun getAllNews(): List<News> {
        // Devolvemos una copia para que nadie desde fuera pueda modificar nuestra lista original.
        return newsList
    }

    /**
     * Busca y devuelve una noticia por su ID en la lista en memoria.
     */
    override fun getNewsById(id: String): News? {
        // El método .find es una forma más limpia de buscar el primer elemento que cumpla una condición.
        return newsList.find { it.id == id }
    }

    /**
     * Función privada para poblar la lista con datos de ejemplo.
     */
    private fun loadSampleData() {
        newsList.add(
            News(
                id = "NWS-001",
                title = "¡Nueva Actualización de la App!",
                description = "Hemos lanzado una nueva versión con mejoras de rendimiento y una interfaz renovada. ¡Actualiza ahora!",
                type = "news",
                affectedServices = listOf("app"),
                startDate = System.currentTimeMillis() - 86400000L, // Hace 1 día
                endDate = 0L, // Sin fecha de fin
                isActive = true
            )
        )
        newsList.add(
            News(
                id = "OUT-002",
                title = "Interrupción del Servicio de Internet en Zona Norte",
                description = "Nuestros técnicos están trabajando para resolver una incidencia masiva que afecta a la conexión de internet en la Zona Norte. Estimamos que el servicio se restablecerá en 3 horas.",
                type = "outage",
                affectedServices = listOf("internet"),
                startDate = System.currentTimeMillis(), // Ahora mismo
                endDate = System.currentTimeMillis() + 10800000L, // En 3 horas
                isActive = true
            )
        )
        newsList.add(
            News(
                id = "MNT-003",
                title = "Mantenimiento Programado de la Red de TV",
                description = "Realizaremos tareas de mantenimiento en la red de televisión por cable durante la madrugada del próximo viernes para mejorar la calidad de la señal. Podrían producirse cortes breves.",
                type = "maintenance",
                affectedServices = listOf("tv"),
                startDate = System.currentTimeMillis() + 259200000L, // En 3 días
                endDate = System.currentTimeMillis() + 262800000L, // Dura 1 hora
                isActive = true
            )
        )
    }
}

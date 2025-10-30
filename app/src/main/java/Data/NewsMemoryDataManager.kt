package Data

import Data.Interfaces.INewsDataManager
import Entity.News/**
 * Implementation of the INewsDataManager contract that stores data
 * in a simple in-memory list. Data is lost if the app is closed.
 *
 */
object NewsMemoryDataManager : INewsDataManager {


    private val newsList = mutableListOf<News>()

    // Initializer block to load sample data when the app starts.
    // This way, the list won't be empty.
    init {
        loadSampleData()
    }

    override fun getAllNews(): List<News> {
        //  Return a copy so that no one from outside can modify our original list.
        return newsList.toList()
    }

    override fun getNewsById(id: String): News? {
        // It returns the first element that matches the condition, or null if not found.
        return newsList.find { it.id == id }
    }

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
package testing

import Data.NewsMemoryDataManager

/**
 * This main function allows us to execute and test the logic of our
 * NewsMemoryDataManager directly in the console.
 */
fun main() {
    println("--- INICIANDO PRUEBAS DEL NewsMemoryDataManager ---")

    // The manager instance we are going to test.
    val manager = NewsMemoryDataManager

    // 1. Test fetching all news items.
    // The manager should have loaded the sample data in its init block.
    println("\n[PASO 1: OBTENER TODAS LAS NOTICIAS]")
    val allNews = manager.getAllNews()
    println("Total de noticias encontradas: ${allNews.size}")
    // Print each news item's title for verification.
    allNews.forEach { news ->
        println("  - Título: ${news.title} (Tipo: ${news.type})")
    }

    // 2. Test searching for a specific news item by its ID (one that exists).
    println("\n[PASO 2: BUSCAR NOTICIA POR ID (ÉXITO)]")
    val outageId = "OUT-002"
    val foundNews = manager.getNewsById(outageId)
    if (foundNews != null) {
        println("Noticia encontrada con ID '$outageId':")
        println("  -> Título: ${foundNews.title}")
        println("  -> Descripción: ${foundNews.description}")
    } else {
        println("Error: No se encontró la noticia con ID '$outageId'")
    }

    // 3. Test searching for a news item by an ID that does NOT exist.
    println("\n[PASO 3: BUSCAR NOTICIA POR ID (FALLO)]")
    val nonExistentId = "FAKE-999"
    val notFoundNews = manager.getNewsById(nonExistentId)
    if (notFoundNews == null) {
        println("Resultado esperado: No se encontró la noticia con ID '$nonExistentId'. ¡Prueba exitosa!")
    } else {
        println("Error en la prueba: Se encontró una noticia que no debería existir.")
    }

    println("\n--- PRUEBAS FINALIZADAS ---")
}

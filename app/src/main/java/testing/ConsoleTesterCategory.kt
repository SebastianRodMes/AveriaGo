package testing

import Data.CategoryMemoryDataManager

/**
 * This main function allows us to execute and test the logic of our
 * CategoryMemoryDataManager directly in the console.
 */
fun main() {
    println("--- INICIANDO PRUEBAS DEL CategoryMemoryDataManager ---")
    val manager = CategoryMemoryDataManager

    // 1. Test fetching all categories.
    // This should return only the active categories ("Internet", "Televisión")
    // and they should be sorted by the 'order' field.
    println("\n[PASO 1: OBTENER TODAS LAS CATEGORÍAS (ACTIVAS Y ORDENADAS)]")
    val allCategories = manager.getAllCategories()

    println("Total de categorías activas encontradas: ${allCategories.size}")
    println("Verificando el orden y contenido:")
    allCategories.forEach { category ->
        println("  - Categoría: ${category.name} (Orden: ${category.order})")
        // check if the subcategories are loaded correctly.
        println("    Subcategorías (${category.subcategories.size}):")
        category.subcategories.forEach { subcategory ->
            println("      -> ${subcategory.name}")
        }
    }

    // 2. Test searching for a specific category by its slug (one that exists).
    println("\n[PASO 2: BUSCAR CATEGORÍA POR SLUG (ÉXITO)]")
    val slugToFind = "television"
    val foundCategory = manager.getCategoryBySlug(slugToFind)
    if (foundCategory != null) {
        println("Resultado esperado: Se encontró la categoría con slug '$slugToFind'.")
        println("  -> Nombre: ${foundCategory.name}")
    } else {
        println("Error en la prueba: No se encontró la categoría con slug '$slugToFind'.")
    }

    // 3. Test searching for a category that is inactive.
    // The "telefonia_movil" category was set to isActive = false.
    // The getCategoryBySlug method should NOT find it.
    println("\n[PASO 3: BUSCAR CATEGORÍA POR SLUG (INACTIVA)]")
    val inactiveSlug = "telefonia_movil"
    val inactiveCategory = manager.getCategoryBySlug(inactiveSlug)
    if (inactiveCategory == null) {
        println("Resultado esperado: No se encontró la categoría con slug '$inactiveSlug' porque está inactiva. ¡Prueba exitosa!")
    } else {
        println("Error en la prueba: Se encontró una categoría que debería estar inactiva.")
    }

    println("\n--- PRUEBAS FINALIZADAS ---")
}

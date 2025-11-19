package Controller

import Data.Interfaces.ICategoryDataManager
import Data.CategoryMemoryDataManager
import Entity.Category
import android.content.Context

class CategoryController(
    private val context: Context,
    private val categoryManager: ICategoryDataManager = CategoryMemoryDataManager
) {

    fun getAllCategories(): List<Category> {
        try {
            return categoryManager.getAllCategories()
        } catch (e: Exception) {
            throw Exception("Error al obtener todas las categorías: ${e.message}")
        }
    }

    fun getCategoryById(id: String): Category {
        try {
            val category = categoryManager.getCategoryById(id)
            if (category != null) {
                return category
            } else {
                throw Exception("No se encontró ninguna categoría con el ID: $id")
            }
        } catch (e: Exception) {
            throw Exception("Error al obtener la categoría por ID: ${e.message}")
        }
    }

    // Ahora usamos directamente el método del manager expuesto por la interfaz
    fun getCategoryBySlug(slug: String): Category? {
        try {
            return categoryManager.getCategoryBySlug(slug)
        } catch (e: Exception) {
            throw Exception("Error al obtener la categoría por slug: ${e.message}")
        }
    }
}
package Controller

import Data.CategoryMemoryDataManager
import Data.Interfaces.ICategoryDataManager
import Entity.Category
import android.content.Context



class CategoryController(private val context: Context) {

    private val categoryManager: ICategoryDataManager = CategoryMemoryDataManager


    fun getAllCategories(): List<Category> {
        try {
            return categoryManager.getAllCategories()
        } catch (e: Exception) {

            throw Exception("Error al obtener todas las categorías")
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

                throw Exception("Error al obtener la categoría por ID")

        }
    }
}

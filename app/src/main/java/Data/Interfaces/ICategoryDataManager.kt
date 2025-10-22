package Data.Interfaces

import Entity.Category

/**
 * Contract that defines the data operations for the Category entity.
 * From the mobile app's perspective, this is primarily a read-only entity.
 */
interface ICategoryDataManager {
    //READ
    fun getAllCategories(): List<Category>
    fun getCategoryById(id: String): Category?
}

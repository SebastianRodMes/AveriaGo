package Data

import Data.Interfaces.ICategoryDataManager
import Entity.Category
import Entity.Subcategory

/**
 * In-memory implementation of the ICategoryDataManager interface.
 * This manager holds a static list of available ticket categories and their subcategories.
 *
 */
object CategoryMemoryDataManager : ICategoryDataManager {

    private val categoryList = mutableListOf<Category>()

    // init block to preload all category data as soon as the manager is accessed.
    init {
        loadSampleCategories()
    }

    /**
     * Returns the complete list of all active categories, sorted by their 'order' field.
     * This is the primary method to be used by the UI to display category options.
     */
    override fun getAllCategories(): List<Category> {
        println("getAllCategories: Devolviendo categorías activas y ordenadas.")
        // Filter only active categories and then sort them based on the 'order' property.
        return categoryList.filter { it.isActive }.sortedBy { it.order }
    }

    override fun getCategoryById(id: String): Category? {
        return categoryList.find { it.categoryId.trim() == id.trim() }
    }

    /**
     * Finds a specific category by its unique slug.
     * This can be useful for deep-linking or internal logic that will be implemented further.
     */
    override fun getCategoryBySlug(slug: String): Category? {
        println("getCategoryBySlug: Buscando categoría con slug '$slug'")
        return categoryList.find { it.slug == slug && it.isActive }
    }

    /**
     * Private function to populate the list with comprehensive sample data.
     * This simulates fetching data from a remote database or local file.
     */
    private fun loadSampleCategories() {
        // --- Internet Category ---
        val internetSubcategories = listOf(
            Subcategory(subcategoryId = "SUB-01", name = "Baja velocidad", order = 1),
            Subcategory(subcategoryId = "SUB-02", name = "No hay conexión", order = 2),
            Subcategory(subcategoryId = "SUB-03", name = "Cortes intermitentes", order = 3)
        )
        val internetCategory = Category(
            categoryId = "CAT-01",
            name = "Internet",
            slug = "internet",
            icon = "ic_wifi", // Example icon name
            color = "#007BFF", // Blue
            order = 1,
            isActive = true,
            subcategories = internetSubcategories
        )

        // --- Television Category ---
        val tvSubcategories = listOf(
            Subcategory(subcategoryId = "SUB-04", name = "Sin señal", order = 1),
            Subcategory(subcategoryId = "SUB-05", name = "Canales faltantes", order = 2),
            Subcategory(subcategoryId = "SUB-06", name = "Mala calidad de imagen", order = 3)
        )
        val tvCategory = Category(
            categoryId = "CAT-02",
            name = "Televisión",
            slug = "television",
            icon = "ic_tv", // Example icon name
            color = "#28A745", // Green
            order = 2,
            isActive = true,
            subcategories = tvSubcategories
        )

        // --- Mobile Phone Category (Inactive Example) ---
        val mobileSubcategories = listOf(
            Subcategory(subcategoryId = "SUB-07", name = "No puedo hacer llamadas", order = 1),
            Subcategory(subcategoryId = "SUB-08", name = "No tengo datos móviles", order = 2)
        )
        val mobileCategory = Category(
            categoryId = "CAT-03",
            name = "Telefonía Móvil",
            slug = "telefonia_movil",
            icon = "ic_phone",
            color = "#DC3545", // Red
            order = 3,
            isActive = false, // This category will be filtered out by getAllCategories()
            subcategories = mobileSubcategories
        )

        // --- Add all categories to the main list ---
        categoryList.add(internetCategory)
        categoryList.add(tvCategory)
        categoryList.add(mobileCategory)

        println("CategoryMemoryDataManager: Datos de ejemplo cargados. ${categoryList.size} categorías en total.")
    }
}

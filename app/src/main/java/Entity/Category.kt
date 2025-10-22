package Entity

data class Category(

    val id: String = "",
    val name: String = "",
    val slug: String = "", //unique identifier and readable instead of using the normal id
    val icon: String = "",
    val color: String = "",
    val order: Int = 0,
    val isActive: Boolean = true,
    val subcategories: List<Subcategory> = emptyList()
)
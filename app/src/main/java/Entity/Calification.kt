package Entity

import Entity.enums.Rating

data class Calification(
    var rating: Rating? = null, // 1-5
    var comment: String = ""
) {

}
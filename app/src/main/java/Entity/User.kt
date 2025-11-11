package Entity

import android.location.Address

data class User(
    val userId: String = generateUserId(),
    var fullName: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var address: String = "",
    var password: String = "",
    var tickets: MutableList<Ticket> = mutableListOf(),
    val contractNumber: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        private var currentId = 0

        private fun generateUserId(): String {
            currentId++
            return "USER_$currentId"
        }
    }

    fun getFullInfo(): String = "$fullName - $email - $phoneNumber - $address - $contractNumber"
    fun isValidUser(): Boolean = email.isNotEmpty() && address.isNotEmpty() && contractNumber.isNotEmpty()
}
package Entity

import android.location.Address

data class User(
    private val userId: String,
    private var  fullName: String,
    private var email: String,
    private var phoneNumber: String,
    private var address: String,
    var tickets: MutableList<Ticket> = mutableListOf(),
    private val contractNumber: String,
    private val createdAt: Long = System.currentTimeMillis()

) {
    fun getFullInfo(): String = "$fullName - $email - $phoneNumber - $address - $contractNumber"
    fun isValidUser(): Boolean = email.isNotEmpty()  && address.isNotEmpty() && contractNumber.isNotEmpty()


}
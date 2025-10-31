package Entity

import android.location.Address

data class User(
     val userId: String,
     var  fullName: String,
     var email: String,
     var phoneNumber: String,
     var address: String,
    var tickets: MutableList<Ticket> = mutableListOf(),
     val contractNumber: String,
     val createdAt: Long = System.currentTimeMillis()

) {
    fun getFullInfo(): String = "$fullName - $email - $phoneNumber - $address - $contractNumber"
    fun isValidUser(): Boolean = email.isNotEmpty()  && address.isNotEmpty() && contractNumber.isNotEmpty()


}
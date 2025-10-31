package Data

import Data.Interfaces.IUserDataManager
import Entity.User

/**
 * In-memory implementation of the IUserDataManager interface.
 *
 *
 * It is primarily used for testing purposes
 * where a real database is not required. It comes pre-populated with sample data.
 */
object UserMemoryDataManager : IUserDataManager {
    private val userList = mutableListOf<User>()

    // Init block to pre-populate the manager with sample data upon first access.
    init {
        userList.add(
            User(
                userId = "USR-001",
                fullName = "Juan Pérez",
                email = "juan.perez@email.com",
                phoneNumber = "61046716",
                address = "Calle Falsa 123, Springfield",
                contractNumber = "C-12345"
            )
        )
    }


    override fun addUser(user: User) {
        println("addUser: Adding user with email ${user.email}...")
        userList.add(user)
        println("addUser: User added! Total users now: ${userList.size}")
    }


    override fun getUserById(id: String): User? {
        // No try-catch needed here as .find is a safe operation that doesn't throw exceptions.
        println("getUserById: Searching for user with ID '$id'...")
        return userList.find { it.userId.trim() == id.trim() }
    }


    override fun getUserByEmail(email: String): User? {
        println("getUserByEmail: Searching for user with email '$email'...")
        return userList.find { it.email.equals(email, ignoreCase = true) }
    }


    override fun getAllUsers(): List<User>? {
        return if (userList.isNotEmpty())
            userList.toList() // Return a read-only copy
        else null
    }


    override fun updateUser(user: User) {
        println("updateUser: Updating user with ID ${user.userId}...")
        val index = userList.indexOfFirst { it.userId.trim() == user.userId.trim() }
        if (index != -1) {
            println("updateUser: User found at index $index. Replacing...")
            userList[index] = user // Replace the old object with the new one
            println("updateUser: User updated!")
        } else {
            println("updateUser: Error - User with ID ${user.userId} not found for update.")
        }
    }


    override fun deleteUser(id: String) {
        println("deleteUser: Attempting to delete user with ID '$id'...")
        val removed = userList.removeIf { it.userId == id }
        if (removed) {
            println("deleteUser: User successfully deleted!")
        } else {
            println("deleteUser: Error - User with ID '$id' not found for deletion.")
        }
    }
}

package Data.Interfaces

import Entity.User

/**
 * Contract that defines the data operations for the User entity.
 * Any class that manages Tickets (in memory, on Firestore, etc.)
 * must implement this interface.
 */
interface IUserDataManager {
    //CREATE
    fun addUser (user: User)

    //READ
    fun getUserById(id: String): User?
    fun getUserByEmail(email: String): User?

    fun getAllUsers(): List<User>?

    //UPDATE
    fun updateUser(user: User)

    //DELETE
    fun deleteUser(id: String)
}
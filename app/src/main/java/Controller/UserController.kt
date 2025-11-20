package Controller

import Data.Interfaces.IUserDataManager
import Data.UserMemoryDataManager
import Data.SharedPrefsUserDataManager
import Entity.User
import android.content.Context

class UserController(
    private val context: Context,
    private val userManager: IUserDataManager
) {

    // Constructor secundario que inyecta la implementación por defecto (SharedPrefs)
    constructor(context: Context) : this(context, SharedPrefsUserDataManager.getInstance(context))

    fun addUser(user: User) {
        try {
            // Evitar duplicados por email
            val existing = userManager.getUserByEmail(user.email)
            if (existing != null) {
                throw Exception("Usuario con este correo ya existe")
            }
            userManager.addUser(user)
        } catch (e: Exception) {
            throw Exception("Error al agregar el usuario: ${e.message}")
        }
    }

    // READ
    fun getUserById(id: String): User? {
        try {
            return userManager.getUserById(id)
        } catch (e: Exception) {
            throw Exception("Error al obtener el usuario")
        }
    }

    fun getUserByEmail(email: String): User? {
        try {
            return userManager.getUserByEmail(email)
        } catch (e: Exception) {
            throw Exception("Error al obtener el usuario")
        }
    }

    fun getAllUsers(): List<User>? {
        try {
            return userManager.getAllUsers()
        } catch (e: Exception) {
            throw Exception("Error al obtener los usuarios")
        }
    }

    // UPDATE
    fun updateUser(user: User) {
        try {
            userManager.updateUser(user)
        } catch (e: Exception) {
            throw Exception("Error al actualizar el usuario")
        }
    }

    // DELETE by email (busca por email y elimina por userId)
    fun deleteUserByEmail(email: String) {
        try {
            val result = userManager.getUserByEmail(email)
            if (result == null) {
                throw Exception("No se encontró el usuario")
            }
            userManager.deleteUser(result.userId)
        } catch (e: Exception) {
            throw Exception("Error al eliminar el usuario")
        }
    }
}
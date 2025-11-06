package Controller

import Data.Interfaces.IUserDataManager
import Data.UserMemoryDataManager
import Entity.User
import android.content.Context

class UserController (private val context: Context) {
    private var userManager: IUserDataManager = UserMemoryDataManager



    fun addUser (user: User){
        try {
            userManager.addUser(user)
        }catch (e: Exception){
            throw Exception("Error al agregar el usuario")
        }
    }

    //READ
    fun getUserById(id: String): User?{
        try {
            return userManager.getUserById(id)

        }catch (e: Exception){
            throw Exception("Error al obtener el usuario")
        }
    }
    fun getUserByEmail(email: String): User?{
        try {
            return userManager.getUserByEmail(email)


        }catch (e: Exception){
            throw Exception("Error al obtener el usuario")
        }
    }

    fun getAllUsers(){
        try {
            userManager.getAllUsers()
        }catch (e: Exception){
            throw Exception("Error al obtener los usuarios")
        }

    }

    //UPDATE
    fun updateUser(user: User){
        try {
           userManager.updateUser(user)
        }catch (e:Exception){
            throw Exception("Error al actualizar el usuario")
        }

    }

    //DELETE
    fun deleteUser(email: String){
        try {
        var result = userManager.getUserByEmail(email)
        if (result == null) {
            throw Exception("No se encontro el usuario")
        }
        userManager.deleteUser(email)
    }catch (e: Exception){
        throw Exception("Error al eliminar el usuario")
    }
    }
}




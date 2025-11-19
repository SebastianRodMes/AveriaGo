package Data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken as GsonTypeToken
import Data.Interfaces.IUserDataManager
import Entity.User

class SharedPrefsUserDataManager private constructor(private val context: Context) : IUserDataManager {

    companion object {
        private const val PREF_NAME = "AveriaguApp_users"
        private const val KEY_USERS = "users_json"
        @Volatile
        private var INSTANCE: SharedPrefsUserDataManager? = null

        fun getInstance(context: Context): SharedPrefsUserDataManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPrefsUserDataManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val prefs: SharedPreferences
        get() = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()
    private val listType = object : GsonTypeToken<MutableList<User>>() {}.type

    private fun loadUsers(): MutableList<User> {
        val json = prefs.getString(KEY_USERS, null) ?: return mutableListOf()
        return try {
            gson.fromJson(json, listType) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    private fun saveUsers(list: List<User>) {
        prefs.edit().putString(KEY_USERS, gson.toJson(list)).apply()
    }

    override fun addUser(user: User) {
        val list = loadUsers()
        list.add(user)
        saveUsers(list)
    }

    override fun getUserById(id: String): User? {
        val list = loadUsers()
        return list.firstOrNull { it.userId == id }
    }

    override fun getUserByEmail(email: String): User? {
        val list = loadUsers()
        return list.firstOrNull { it.email.equals(email, ignoreCase = true) }
    }

    override fun getAllUsers(): List<User>? {
        return loadUsers()
    }

    override fun updateUser(user: User) {
        val list = loadUsers()
        val idx = list.indexOfFirst { it.userId == user.userId }
        if (idx >= 0) {
            list[idx] = user
            saveUsers(list)
        } else {
            throw Exception("Usuario no encontrado")
        }
    }

    override fun deleteUser(id: String) {
        val list = loadUsers()
        val removed = list.removeAll { it.userId == id }
        if (removed) {
            saveUsers(list)
        } else {
            throw Exception("Usuario no encontrado")
        }
    }
}
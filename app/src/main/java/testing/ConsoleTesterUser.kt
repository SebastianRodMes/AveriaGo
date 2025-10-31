package testing

import Data.UserMemoryDataManager
import Entity.User

/**
 * This main function allows us to execute and test the logic of our
 * UserMemoryDataManager directly in the console, without needing to run
 * the full Android application. */
fun main() {
    println("--- INICIANDO PRUEBAS DEL UserMemoryDataManager ---")

    // The manager instance we are going to test.
    val manager = UserMemoryDataManager

    // 1. Verify the initial state of the manager.
    // It contains the preloaded user ("Juan Pérez").
    println("\n[PASO 1: ESTADO INICIAL]")
    println("Usuarios actuales: ${manager.getAllUsers()}")

    // 2. Test adding a new user.
    println("\n[PASO 2: AÑADIR USUARIO]")
    val newUser = User(
        userId = "USR-002",
        fullName = "Ana López",
        email = "ana.lopez@email.com",
        phoneNumber = "654321987",
        address = "Avenida Siempreviva 742",
        contractNumber = "C-67890"
    )
    manager.addUser(newUser)
    println("Lista de usuarios después de añadir: ${manager.getAllUsers()}")

    // 3. Test searching for a user by their ID (one that we know exists).
    println("\n[PASO 3: BUSCAR POR ID (ÉXITO)]")
    val foundUser = manager.getUserById("USR-001")
    println("Usuario encontrado por ID 'USR-001': ${foundUser?.fullName ?: "Ninguno"}")

    // 4. Test searching for a user by their email address.
    println("\n[PASO 4: BUSCAR POR EMAIL]")
    val userByEmail = manager.getUserByEmail("ana.lopez@email.com")
    println("Usuario encontrado por email 'ana.lopez@email.com': ${userByEmail?.fullName ?: "Ninguno"}")

    // 5. Test updating an existing user's information.
    // We use the 'copy()' function from the data class to create a modified version of the user.
    println("\n[PASO 5: ACTUALIZAR USUARIO]")
    val updatedUser = foundUser!!.copy(fullName = "Juan Pérez Actualizado")
    manager.updateUser(updatedUser)
    println("Usuario 'USR-001' después de actualizar: ${manager.getUserById("USR-001")}")

    // 6. Test deleting a user from the list.
    println("\n[PASO 6: ELIMINAR USUARIO]")
    manager.deleteUser("USR-002")
    println("Lista final de usuarios: ${manager.getAllUsers()}")

    println("\n--- PRUEBAS FINALIZADAS ---")
}

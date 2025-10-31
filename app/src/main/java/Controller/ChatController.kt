package Controller

import Data.ChatMemoryDataManager
import Data.Interfaces.IChatDataManager
import Entity.Chat
import android.content.Context

class ChatController (private val context: Context) {
    private var chatManager: IChatDataManager = ChatMemoryDataManager

    //CREATE
    fun addChat(chat: Chat){
        try {
               chatManager.addChat(chat)

        }catch (e: Exception){
            throw Exception("Error al agregar el chat")
        }
    }

    //READ
    fun getChatById(id: String): Chat {
        try {
            var result = chatManager.getChatById(id)
            if (result == null) {
                throw Exception("No se encontro el chat")
            }
            return result
        } catch (e: Exception) {
            throw Exception("Error al obtener el chat")
        }
    }

    fun getChatByTicketId(ticketId: String): Chat{
        try {
            var result = chatManager.getChatByTicketId(ticketId)
            if (result == null) {
                throw Exception("No se encontro el chat")
            }
            return result
        } catch (e: Exception) {
            throw Exception("Error al obtener el chat")
        }
    }

    fun getChatsByUserId(userId: String): List<Chat>{
        try {
            var result = chatManager.getChatsByUserId(userId)
            if (result == null) {
                throw Exception("No se encontraron chats")
            }
            return result
        } catch (e: Exception) {
            throw Exception("Error al obtener los chats")
        }

    }

    //UPDATE
    fun updateChat(chat: Chat){
        try {
            var chatExists = chatManager.getChatById(chat.id)
            if (chatExists == null) {
                throw Exception("No se encontro el chat")
            }
            chatManager.updateChat(chat)
        }catch (e:Exception){
            throw Exception("Error al actualizar el chat")
        }
    }


    }

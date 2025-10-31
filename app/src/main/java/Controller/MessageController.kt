package Controller

import Data.Interfaces.IMessageDataManager
import Data.MessageMemoryDataManager
import Entity.Message
import android.content.Context

class MessageController(private val context: Context) {
    private var messageManager: IMessageDataManager = MessageMemoryDataManager

    //CREATE
    fun addMessage(chatId: String, message: Message){
        try {
            messageManager.addMessage(chatId, message)
        }catch (e: Exception){
            throw Exception("Error al agregar el mensaje")
        }

    }

    //READ
    fun getMessagesByChatId(chatId: String): List<Message>{
        try {
            var result = messageManager.getMessagesByChatId(chatId)
            if (result == null) {
                throw Exception("No se encontraron mensajes")
            }
            return result
            }catch (e: Exception){
            throw Exception("Error al obtener los mensajes")
        }
    }


    fun listenForMessages(chatId: String, onUpdate: (List<Message>) -> Unit){
        try {
            messageManager.listenForMessages(chatId, onUpdate)
        }catch (e: Exception){
            throw Exception("Error al escuchar los mensajes")
        }
    }
}


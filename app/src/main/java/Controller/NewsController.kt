package Controller

import Data.Interfaces.INewsDataManager
import Data.NewsMemoryDataManager
import Entity.News
import android.content.Context

class NewsController(context: Context) {
    private val newsManager: INewsDataManager = NewsMemoryDataManager

    fun getAllNews(): List<News>{
        try {
            return newsManager.getAllNews()
        }
        catch (e: Exception){
            throw Exception("Error al obtener las noticias")
        }
    }

    fun getNewsById(id: String): News{
        try {
            var result = newsManager.getNewsById(id)
            if (result == null) {
                throw Exception("No se encontro la noticia")
            }
            return result
    }catch (e: Exception){
        throw Exception("Error al obtener la noticia")
        }
    }
}

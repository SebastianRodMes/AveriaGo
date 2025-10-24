package Data.Interfaces

import Entity.News

/**
 * Contract that defines the data operations for the News entity.
 * From the mobile app's perspective, this is a read-only entity used
 * to inform users about outages, maintenance, or general news.
 */
interface INewsDataManager {

    fun getAllNews(): List<News>
    fun getNewsById(id: String): News?
}

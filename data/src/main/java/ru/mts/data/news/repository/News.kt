package ru.mts.data.news.repository

import ru.mts.data.news.db.NewsEntity

data class News(
    val id: Int,
    val text: String,
) {

    fun toEntity() = NewsEntity(id, text)
}

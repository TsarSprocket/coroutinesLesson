package ru.mts.data.news.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mts.data.news.repository.News

const val ILLEGAL_ID = -1

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "text") val text: String,
)

fun NewsEntity.toDomain() = News(id, text)
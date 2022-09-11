package ru.mts.data.news.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mts.data.news.db.NewsLocalDataSource
import ru.mts.data.news.db.toDomain
import ru.mts.data.news.remote.NewsRemoteDataSource
import ru.mts.data.news.remote.toDomain
import ru.mts.data.utils.Result

class NewsRepository(
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource
) {
    suspend fun getNews(forceRefresh: Boolean): Flow<Result<List<News>, Throwable>> {
        return flow {
            if(!forceRefresh) {
                val localNews = newsLocalDataSource.getNews()

                if(localNews is Result.Success && localNews.data.isNotEmpty()) {
                    emit(Result.Success(localNews.data.map { it.toDomain() }))
                    return@flow
                }
            }

            val remoteNews = newsRemoteDataSource.getNews()

            if(remoteNews is Result.Success) {
                val news = remoteNews.data.toDomain()
                newsLocalDataSource.storeNews(news.toEntity())
                when(val freshNews = newsLocalDataSource.getNews()) {
                    is Result.Success -> emit(Result.Success(freshNews.data.map { it.toDomain() }))
                    is Result.Error -> emit(Result.Error(freshNews.error))
                }
            } else {
                (remoteNews as? Result.Error)?.let { emit(Result.Error(it.error)) }
            }
        }
    }
}
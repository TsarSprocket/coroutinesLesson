package ru.mts.data.news.remote

import kotlinx.coroutines.delay
import ru.mts.data.main.NetworkClient
import ru.mts.data.utils.Result
import ru.mts.data.utils.runOperationCatching

class NewsRemoteDataSource {
    suspend fun getNews(): Result<NewsDto.Response, Throwable> {
        return runOperationCatching {
            if(Math.random() < 0.5) throw RuntimeException("Sh%t happened")
            delay(3000L)
            NetworkClient.create().getSampleData(NewsDto.Request(1))
        }
    }
}
package com.test.marvelcomics.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.test.marvelcomics.data.database.ComicsDatabase
import com.test.marvelcomics.data.network.MarvelComicsNetworkRepository
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters
import kotlinx.coroutines.flow.Flow

class MarvelComicsRepository(
    private val networkRepository: MarvelComicsNetworkRepository,
    private val database: ComicsDatabase
) {
    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getComicsData(dataRange: String): Flow<PagingData<ComicWithWritersAndPainters>> {
        val addStrForDate = "T00:00:00-0400"
        val massiveDataRange = dataRange.split(",", limit = 2)
        val startRange = massiveDataRange[0] + addStrForDate
        val endRange = massiveDataRange[1] + addStrForDate
        val pagingSourceFactory = {
            database.comicDao().getComics(startRange, endRange)
        }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 20
            ),
            remoteMediator = MarvelComicsRemoteMediator(
                dataRange,
                networkRepository,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}
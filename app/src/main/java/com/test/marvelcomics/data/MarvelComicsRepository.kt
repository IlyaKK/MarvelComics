package com.test.marvelcomics.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.test.marvelcomics.data.database.ComicsDatabase
import com.test.marvelcomics.data.network.MarvelComicsNetworkRepository
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters
import com.test.marvelcomics.util.UtilData
import kotlinx.coroutines.flow.Flow

class MarvelComicsRepository(
    private val networkRepository: MarvelComicsNetworkRepository,
    private val database: ComicsDatabase
) {
    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalPagingApi::class)
    fun getComicsData(dataRange: String): Flow<PagingData<ComicWithWritersAndPainters>> {
        val massiveDataRange = UtilData.createMassiveStringDataRangeForItemOfList(dataRange)
        val pagingSourceFactory = {
            database.comicDao().getComics(massiveDataRange[0], massiveDataRange[1])
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
package com.test.marvelcomics.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.test.marvelcomics.data.MarvelComicsRepository.Companion.NETWORK_PAGE_SIZE
import com.test.marvelcomics.data.database.ComicsDatabase
import com.test.marvelcomics.data.retrofit.MarvelComicsService
import com.test.marvelcomics.data.retrofit.MarvelNetworkSecurity
import com.test.marvelcomics.domain.entity.api.ComicApi
import com.test.marvelcomics.domain.entity.database.*
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MarvelComicsRemoteMediator(
    private val dataRange: String,
    private val service: MarvelComicsService,
    private val marvelComicsDatabase: ComicsDatabase
) : RemoteMediator<Int, ComicWithWritersAndPainters>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ComicWithWritersAndPainters>
    ): MediatorResult {
        var limit = state.config.pageSize
        val offset: Int

        when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                if (remoteKeys != null) {
                    offset = remoteKeys.nextKey?.minus(NETWORK_PAGE_SIZE) ?: 0
                    limit = state.config.pageSize
                } else {
                    offset = 0
                    limit = state.config.initialLoadSize
                }
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                offset = prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                offset = nextKey
            }
        }

        try {
            val marvelNetworkSecurity = MarvelNetworkSecurity()
            val apiResponse = service.getPublishedComics(
                nowDate = dataRange,
                formatType = "comic",
                orderBy = "-onsaleDate",
                offset = offset,
                limit = limit,
                timeStamp = marvelNetworkSecurity.timeStamp,
                apiKey = marvelNetworkSecurity.publicMarvelApiKey,
                hash = marvelNetworkSecurity.hashMd5ForMarvelRequest
            )

            val repos = apiResponse.dataComicsApi.comicsList
            val endOfPaginationReached = repos.isEmpty()
            marvelComicsDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    marvelComicsDatabase.remoteKeysDao().clearRemoteKeys()
                    marvelComicsDatabase.comicDao().clearComicPainterCrossRef()
                    marvelComicsDatabase.comicDao().clearComicWriterCrossRef()
                    marvelComicsDatabase.painterDao().clearPainter()
                    marvelComicsDatabase.writerDao().clearWriter()
                    marvelComicsDatabase.comicDao().clearComics()
                }
                val prevKey = if (offset == 0) null else offset
                val nextKey = if (endOfPaginationReached) null else offset + limit
                val keys = repos.map {
                    RemoteKeys(comicId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                marvelComicsDatabase.remoteKeysDao().insertAll(keys)
                insertComics(repos)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun insertComics(repos: List<ComicApi>) {
        repos.forEach { comicApi ->
            val listWritersDb: MutableList<WriterEntityDb> = mutableListOf()
            val listPaintersDb: MutableList<PainterEntityDb> = mutableListOf()
            comicApi.apply {
                var urlDetailComic: String? = null
                var saleDayComic: String? = null
                var priceComic: Double? = null
                var descriptionComic: String?

                urls.forEach { urlsAboutComic ->
                    if (urlsAboutComic.type == "detail") urlDetailComic =
                        urlsAboutComic.url
                }

                dates.forEach { datesOfComic ->
                    if (datesOfComic.type == "onsaleDate") {
                        saleDayComic = datesOfComic.date
                    }
                }

                prices.forEach { price ->
                    if (price.type == "printPrice") priceComic = price.price
                }

                descriptionComic = description

                textObjects?.forEach {
                    it?.let {
                        if (!it.text.isNullOrEmpty()) {
                            descriptionComic = it.text
                        }
                    }
                }

                val comicDb = ComicEntityDb(
                    id,
                    title,
                    issueNumber,
                    descriptionComic,
                    format,
                    pageCount,
                    urlDetailComic,
                    saleDayComic,
                    priceComic,
                    imageApiPath.path
                )

                creatorsComicApi.listCreatorsComicApi.forEach { creatorComic ->
                    when (creatorComic.role) {
                        "writer" -> {
                            listWritersDb.add(WriterEntityDb(0, creatorComic.name))
                        }
                        "penciler (cover)",
                        "penciler",
                        "penciller (cover)",
                        "penciller" -> {
                            listPaintersDb.add(PainterEntityDb(0, creatorComic.name))
                        }
                    }
                }

                marvelComicsDatabase.comicDao().insertComic(comicDb)

                listWritersDb.forEach {
                    marvelComicsDatabase.writerDao().insert(it)
                    val idWriter = marvelComicsDatabase.writerDao().getIdWriterByName(it.name)
                    marvelComicsDatabase.comicDao().insertComicWriterCrossRef(
                        ComicWriterCrossRef(
                            comicId = comicDb.comicId,
                            writerId = idWriter
                        )
                    )
                }

                listPaintersDb.forEach {
                    marvelComicsDatabase.painterDao().insert(it)
                    val idPainter =
                        marvelComicsDatabase.painterDao().getIdPainterByName(it.name)
                    marvelComicsDatabase.comicDao().insertComicPainterCrossRef(
                        ComicPainterCrossRef(
                            comicId = comicDb.comicId,
                            painterId = idPainter
                        )
                    )
                }
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ComicWithWritersAndPainters>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { comicWithWritersAndPainters ->
                marvelComicsDatabase.remoteKeysDao()
                    .remoteKeysComicId(comicWithWritersAndPainters.comic.comicId)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ComicWithWritersAndPainters>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { comicWithWritersAndPainters ->
                marvelComicsDatabase.remoteKeysDao()
                    .remoteKeysComicId(comicWithWritersAndPainters.comic.comicId)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ComicWithWritersAndPainters>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.comic?.comicId?.let { comicId ->
                marvelComicsDatabase.remoteKeysDao().remoteKeysComicId(comicId)
            }
        }
    }
}
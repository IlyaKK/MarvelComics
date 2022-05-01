package com.test.marvelcomics.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.gson.JsonSyntaxException
import com.test.marvelcomics.data.database.ComicsDatabase
import com.test.marvelcomics.data.network.MarvelComicsNetworkRepository
import com.test.marvelcomics.domain.entity.api.ComicApi
import com.test.marvelcomics.domain.entity.database.*
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MarvelComicsRemoteMediator(
    private val dataRange: String,
    private val networkRepository: MarvelComicsNetworkRepository,
    private val marvelComicsDatabase: ComicsDatabase
) : RemoteMediator<Int, ComicWithWritersAndPainters>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ComicWithWritersAndPainters>
    ): MediatorResult {
        var limit = state.config.pageSize
        var offset = 0

        when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                offset = remoteKeys?.nextOffset?.minus(limit) ?: 0
                if (remoteKeys == null) {
                    limit = state.config.initialLoadSize
                }
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                offset = remoteKeys?.prevOffset
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                offset = remoteKeys?.nextOffset
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val apiResponse = networkRepository.getPublishedComics(
                dataRange = dataRange,
                offset = offset,
                limit = limit
            )

            val repos = apiResponse.dataComicsApi.comicsList
            val endOfPaginationReached = repos.isEmpty()
            marvelComicsDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    marvelComicsDatabase.remoteKeysDao().clearRemoteKeys()
                    marvelComicsDatabase.painterDao().clearPainter()
                    marvelComicsDatabase.writerDao().clearWriter()
                    marvelComicsDatabase.comicDao().clearComicWriterCrossRef()
                    marvelComicsDatabase.comicDao().clearComicPainterCrossRef()
                    marvelComicsDatabase.comicDao().clearComics()
                }
                val listComicsEntityDb = createListComicsDb(repos)
                val prevOffset = if (offset == 0) null else offset - limit
                val nextOffset =
                    if (endOfPaginationReached) null else offset + limit
                if (listComicsEntityDb.isNotEmpty()) {
                    val keys = listComicsEntityDb.map {
                        (RemoteKeys(it.comicId, prevOffset, nextOffset))
                    }
                    marvelComicsDatabase.remoteKeysDao().insertAll(keys)
                    marvelComicsDatabase.comicDao().insertAll(listComicsEntityDb)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        } catch (exception: JsonSyntaxException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun createListComicsDb(
        repos: List<ComicApi>
    ): List<ComicEntityDb> {
        val listComicsDb = mutableListOf<ComicEntityDb>()
        repos.forEach { comicApi ->
            val listWritersDb: MutableList<WriterEntityDb> = mutableListOf()
            val listPaintersDb: MutableList<PainterEntityDb> = mutableListOf()
            if (!existComicInDatabase(comicApi.id)) {
                comicApi.apply {
                    var urlDetailComic: String? = null
                    var saleDayComic: String? = null
                    var priceComic: Double? = null
                    var descriptionComic: String?

                    urls?.forEach { urlsAboutComic ->
                        if (urlsAboutComic?.type == "detail") urlDetailComic =
                            urlsAboutComic.url
                    }

                    dates?.forEach { datesOfComic ->
                        if (datesOfComic?.type == "onsaleDate") {
                            saleDayComic = datesOfComic.date
                        }
                    }

                    prices?.forEach { price ->
                        if (price?.type == "printPrice") priceComic = price.price
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
                        imageApiPath?.path,
                        timeDownload
                    )

                    listComicsDb.add(comicDb)

                    creatorsComicApi?.listCreatorsComicApi?.forEach { creatorComic ->
                        when (creatorComic?.role) {
                            "writer" -> {
                                creatorComic.name?.let { WriterEntityDb(0, it) }
                                    ?.let { listWritersDb.add(it) }
                            }
                            "penciler (cover)",
                            "penciler",
                            "penciller (cover)",
                            "penciller" -> {
                                creatorComic.name?.let { PainterEntityDb(0, it) }
                                    ?.let { listPaintersDb.add(it) }
                            }
                        }
                    }

                    marvelComicsDatabase.withTransaction {
                        listWritersDb.forEach {
                            marvelComicsDatabase.writerDao().insert(it)
                            val idWriter =
                                marvelComicsDatabase.writerDao().getIdWriterByName(it.name)
                            marvelComicsDatabase.comicDao().insertComicWriterCrossRef(
                                ComicWriterCrossRef(
                                    comicId = comicDb.comicId,
                                    writerId = idWriter
                                )
                            )
                        }
                    }

                    marvelComicsDatabase.withTransaction {
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
        }
        return listComicsDb.toList()
    }

    private suspend fun existComicInDatabase(id: Int): Boolean {
        return marvelComicsDatabase.comicDao().getComicById(id) != null
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ComicWithWritersAndPainters>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()
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
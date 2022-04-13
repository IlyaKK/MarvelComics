package com.test.marvelcomics.data

import com.test.marvelcomics.domain.entity.api.Comic
import com.test.marvelcomics.domain.entity.database.ComicEntityDb
import com.test.marvelcomics.domain.entity.database.PainterEntityDb
import com.test.marvelcomics.domain.entity.database.WriterEntityDb
import com.test.marvelcomics.domain.repo.MarvelComicsRepository

class MarvelComicsRepositoryImpl(
    networkRepository: NetworkMarvelComicsRepository,
    databaseRepository: DataBaseComicsRepository
) : MarvelComicsRepository {

    private var comicDatabaseRepo: DataBaseComicsRepository = databaseRepository

    private var comicNetworkRepo: NetworkMarvelComicsRepository = networkRepository

    override fun getComicsData(
        stateInternet: Boolean,
        dataRange: String,
        offset: Int,
        callback: (List<Comic>) -> Unit
    ) {
        if (stateInternet) {
            comicNetworkRepo.getMarvelComics(dataRange, offset) {
                it?.let { comicsApiList ->
                    callback(it)
                    Thread {
                        saveInDatabase(comicsApiList)
                    }.start()
                }
            }
        }
    }

    private fun saveInDatabase(comicsApiList: List<Comic>) {
        comicsApiList.forEach { comicApi ->
            val listWritersDb: MutableList<WriterEntityDb> = mutableListOf()
            val listPaintersDb: MutableList<PainterEntityDb> = mutableListOf()
            comicApi.apply {
                var urlDetailComic: String? = null
                var saleDayComic: String? = null
                var priceComic: Double? = null
                urls.forEach { urlsAboutComic ->
                    if (urlsAboutComic.type == "detail") urlDetailComic =
                        urlsAboutComic.url
                }
                dates.forEach { datesOfComic ->
                    if (datesOfComic.type == "onsaleDate") saleDayComic =
                        datesOfComic.date
                }
                prices.forEach { price ->
                    if (price.type == "printPrice") priceComic = price.price
                }
                val comicDb = ComicEntityDb(
                    id,
                    title,
                    issueNumber,
                    description,
                    format,
                    pageCount,
                    text,
                    urlDetailComic,
                    saleDayComic,
                    priceComic,
                    imagePath.path
                )
                creatorsComic.listCreatorsComic.forEach { creatorComic ->
                    when (creatorComic.role) {
                        "writer" -> {
                            listWritersDb.add(WriterEntityDb(null, creatorComic.name))
                        }
                        "penciler (cover)",
                        "penciler",
                        "penciller (cover)",
                        "penciller" -> {
                            listPaintersDb.add(PainterEntityDb(null, creatorComic.name))
                        }
                    }
                }

                comicDatabaseRepo.insertComics(comicDb)
                comicDatabaseRepo.insertWriters(comicDb.id, listWritersDb)
                comicDatabaseRepo.insertPainters(comicDb.id, listPaintersDb)
            }
        }
    }
}
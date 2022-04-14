package com.test.marvelcomics.data

import com.test.marvelcomics.domain.entity.Comic
import com.test.marvelcomics.domain.entity.api.ComicApi
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
                    Thread {
                        saveInDatabase(comicsApiList)
                        callback(getFromDataBase())
                    }.start()
                }
            }
        } else {
            Thread {
                callback(getFromDataBase())
            }.start()
        }
    }

    private fun getFromDataBase(): List<Comic> {
        val listComics: MutableList<Comic> = mutableListOf()
        val listComicDatabase = comicDatabaseRepo.getComics()
        listComicDatabase.forEach { comicDb ->
            comicDb.apply {
                var descriptionComic: String? = null
                val listWriters: MutableList<String> = mutableListOf()
                val listPainters: MutableList<String> = mutableListOf()
                if (description == null && solicitText == null && previewText == null) {
                    descriptionComic = null
                } else if (description != null && solicitText == null && previewText == null) {
                    descriptionComic = description
                } else if (description == null && solicitText != null && previewText == null) {
                    descriptionComic = solicitText
                } else if (description == null && solicitText == null && previewText != null) {
                    descriptionComic = previewText
                } else if (description != null && solicitText != null && previewText != null) {
                    descriptionComic = solicitText
                }

                comicDatabaseRepo.getWritersOfComic(id).forEach {
                    listWriters.add(it.name)
                }

                comicDatabaseRepo.getPaintersOfComic(id).forEach {
                    listPainters.add(it.name)
                }
                val comic = Comic(
                    id,
                    title,
                    issueNumber,
                    descriptionComic,
                    format,
                    pageCount,
                    urlDetail,
                    saleDay,
                    price,
                    imagePath,
                    listWriters,
                    listPainters
                )
                listComics.add(comic)
            }
        }
        return listComics.toList()
    }

    private fun saveInDatabase(comicsApiList: List<ComicApi>) {
        comicsApiList.forEach { comicApi ->
            val listWritersDb: MutableList<WriterEntityDb> = mutableListOf()
            val listPaintersDb: MutableList<PainterEntityDb> = mutableListOf()
            comicApi.apply {
                var urlDetailComic: String? = null
                var saleDayComic: String? = null
                var priceComic: Double? = null
                var solicitText: String? = null
                var previewText: String? = null
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
                textObjects?.forEach { additionalDescriptionComic ->
                    if (additionalDescriptionComic.type == "issue_solicit_text") {
                        solicitText = additionalDescriptionComic.text
                    } else if (additionalDescriptionComic.type == "issue_preview_text") {
                        previewText = additionalDescriptionComic.text
                    }
                }
                val comicDb = ComicEntityDb(
                    id,
                    title,
                    issueNumber,
                    description,
                    format,
                    pageCount,
                    solicitText,
                    previewText,
                    urlDetailComic,
                    saleDayComic,
                    priceComic,
                    imageApiPath.path
                )
                creatorsComicApi.listCreatorsComicApi.forEach { creatorComic ->
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
package com.test.marvelcomics.data

import com.test.marvelcomics.data.database.*
import com.test.marvelcomics.domain.entity.database.*

class DataBaseComicsRepository(
    private val comicDao: ComicDao,
    private val writerDao: WriterDao,
    private val painterDao: PainterDao,
    private val writerJoinDao: ComicWriterJoinDao,
    private val painterJoinDao: ComicPainterJoinDao
) {

    fun getComics(dataRange: String): List<ComicEntityDb> {
        val addStrForDate = "T00:00:00-0400"
        val massiveDataRange = dataRange.split(",", limit = 2)
        val startRange = massiveDataRange[0] + addStrForDate
        val endRange = massiveDataRange[1] + addStrForDate
        return comicDao.getComics(startRange, endRange)
    }

    fun insertComics(comic: ComicEntityDb) {
        val listIdComic = comicDao.getComicsId()
        if (!listIdComic.contains(comic.id)) {
            comicDao.insert(comic)
        }
    }

    fun getWritersOfComic(idComic: Int): List<WriterEntityDb> {
        return writerJoinDao.getWritersForComic(idComic)
    }

    fun insertWriters(idComic: Int, writers: List<WriterEntityDb>) {
        val listIdComic = comicDao.getComicsId()
        val listNameWriters = writerDao.getWritersName()
        if (listIdComic.contains(idComic)) {
            writers.forEach {
                if (!listNameWriters.contains(it.name)) {
                    writerDao.insert(it)
                }
                val idWriter = writerDao.getIdByName(it.name)
                checkComicWriterJoinExist(idComic, idWriter)
            }
        }
    }

    private fun checkComicWriterJoinExist(idComic: Int, idWriter: Int) {
        val listComicWriter = writerJoinDao.getWriterComic()
        var count = 0
        listComicWriter.forEach { comicWriterJoinDb ->
            if (comicWriterJoinDb.comicId == idComic && comicWriterJoinDb.writerId == idWriter) {
                count++
            }
        }
        if (count == 0) writerJoinDao.insert(ComicWriterJoinDb(0, idComic, idWriter))
    }


    fun getPaintersOfComic(idComic: Int): List<PainterEntityDb> {
        return painterJoinDao.getPaintersForComic(idComic)
    }

    fun insertPainters(idComic: Int, painters: List<PainterEntityDb>) {
        val listIdComic = comicDao.getComicsId()
        val listNamePainters = painterDao.getPaintersName()
        if (listIdComic.contains(idComic)) {
            painters.forEach {
                if (!listNamePainters.contains(it.name)) {
                    painterDao.insert(it)
                }
                val idPainter = painterDao.getIdByName(it.name)
                checkComicPainterJoinExist(idComic, idPainter)
            }
        }
    }

    private fun checkComicPainterJoinExist(idComic: Int, idPainter: Int) {
        val listComicPainter = painterJoinDao.getPainterComic()
        var count = 0
        listComicPainter.forEach { comicPainterJoinDb ->
            if (comicPainterJoinDb.comicId == idComic && comicPainterJoinDb.painterId == idPainter) {
                count++
            }
        }
        if (count == 0) painterJoinDao.insert(ComicPainterJoinDb(0, idComic, idPainter))
    }
}
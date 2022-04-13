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

    fun getComics(): List<ComicEntityDb> {
        return emptyList()
    }

    fun insertComics(comic: ComicEntityDb) {
        val listIdComic = comicDao.getComicsId()
        if (!listIdComic.contains(comic.id)) {
            comicDao.insert(comic)
        }
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
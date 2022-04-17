package com.test.marvelcomics.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.marvelcomics.domain.entity.database.ComicEntityDb
import com.test.marvelcomics.domain.entity.database.ComicPainterJoinDb
import com.test.marvelcomics.domain.entity.database.PainterEntityDb

@Dao
interface ComicPainterJoinDao {
    @Insert
    fun insert(comicPainterJoinDb: ComicPainterJoinDb)

    @Query("SELECT * FROM comic_painter_join")
    fun getPainterComic(): List<ComicPainterJoinDb>

    @Query(
        "SELECT painter.id, painter.name FROM painter " +
                "INNER JOIN comic_painter_join " +
                "ON painter.id = comic_painter_join.painter_id " +
                "WHERE comic_painter_join.comic_id = :comicId"
    )
    fun getPaintersForComic(comicId: Int): List<PainterEntityDb>
}
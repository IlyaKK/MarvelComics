package com.test.marvelcomics.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.marvelcomics.domain.entity.database.ComicEntityDb
import com.test.marvelcomics.domain.entity.database.ComicWriterJoinDb
import com.test.marvelcomics.domain.entity.database.WriterEntityDb

@Dao
interface ComicWriterJoinDao {
    @Insert
    fun insert(comicWriterJoinDb: ComicWriterJoinDb)

    @Query("SELECT * FROM comic_writer_join")
    fun getWriterComic(): List<ComicWriterJoinDb>

    @Query(
        "SELECT writer.id, writer.name FROM writer " +
                "INNER JOIN comic_writer_join " +
                "ON writer.id = comic_writer_join.writer_id " +
                "WHERE comic_writer_join.comic_id = :comicId"
    )
    fun getWritersForComic(comicId: Int): List<WriterEntityDb>
}
package com.test.marvelcomics.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.test.marvelcomics.domain.entity.database.ComicEntityDb

@Dao
interface ComicDao {
    @Insert
    fun insert(comic: ComicEntityDb)

    @Query("SELECT id FROM comic")
    fun getComicsId(): List<Int>

    @Query(
        "SELECT * FROM comic " +
                "ORDER BY saleDay DESC"
    )
    fun getComics(): List<ComicEntityDb>

    @Delete
    fun delete(comic: ComicEntityDb)
}